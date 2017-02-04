package com.flir.SoftheonExampleFlirOneApplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.flir.SoftheonExampleFlirOneApplication.util.SystemUiHider;
import com.flir.flironesdk.Device;
import com.flir.flironesdk.FlirUsbDevice;
import com.flir.flironesdk.Frame;
import com.flir.flironesdk.FrameProcessor;
import com.flir.flironesdk.LoadedFrame;
import com.flir.flironesdk.RenderedImage;
import com.flir.flironesdk.SimulatedDevice;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This is the main activity and contains all delegate methods for the Thermal Imaging Camera Device
 */
public class MainActivity extends Activity implements Device.Delegate, FrameProcessor.Delegate, Device.StreamDelegate, Device.PowerUpdateDelegate {
    //region Private Members

    //Image View to display the camera video feed
    ImageView thermalImageView;

    //Whether the user requested the image to be captured
    private volatile boolean imageCaptureRequested = false;

    //Whether the device's charging cable is connected
    private boolean chargeCableIsConnected = true;

    //Event listener for device orientation
    private OrientationEventListener orientationEventListener;

    //The thermal imaging camera device
    private volatile Device flirOneDevice;

    //Converts from Frame to RenderedImage Processing, includes MSX fusion, colorizing and temperature-accurate thermography
    private FrameProcessor frameProcessor;

    //The last saved path
    private String lastSavedPath;

    //Initialize the device's current tuning state to unknown
    private Device.TuningState currentTuningState = Device.TuningState.Unknown;

    //Reference tot he color filter
    private ColorFilter originalChargingIndicatorColor = null;

    //The thermal bitmap
    private Bitmap thermalBitmap = null;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    //endregion

    //region Activity Override Methods
    @Override
    protected void onStart() {
        super.onStart();
        thermalImageView = (ImageView) findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.imageView);
        if (Device.getSupportedDeviceClasses(this).contains(FlirUsbDevice.class)) {
            findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.pleaseConnect).setVisibility(View.VISIBLE);
        }
        try {
            Device.startDiscovery(this, this);
        } catch (IllegalStateException e) {
            // it's okay if we've already started discovery
        } catch (SecurityException e) {
            // On some platforms, we need the user to select the app to give us permisison to the USB device.
            Toast.makeText(this, "Please insert FLIR One and select " + getString(com.flir.SoftheonExampleFlirOneApplication.R.string.app_name), Toast.LENGTH_LONG).show();
            // There is likely a cleaner way to recover, but for now, exit the activity and
            // wait for user to follow the instructions;
            finish();
        }
    }

    ScaleGestureDetector mScaleDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(com.flir.SoftheonExampleFlirOneApplication.R.layout.activity_preview);

        final View controlsView = findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.fullscreen_content_controls);
        final View controlsViewTop = findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.fullscreen_content_controls_top);
        final View contentView = findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.fullscreen_content);


        HashMap<Integer, String> imageTypeNames = new HashMap<>();
        // Massage the type names for display purposes and skip any deprecated
        for (Field field : RenderedImage.ImageType.class.getDeclaredFields()) {
            if (field.isEnumConstant() && !field.isAnnotationPresent(Deprecated.class)) {
                RenderedImage.ImageType t = RenderedImage.ImageType.valueOf(field.getName());
                String name = t.name().replaceAll("(RGBA)|(YCbCr)|(8)", "").replaceAll("([a-z])([A-Z])", "$1 $2");
                imageTypeNames.put(t.ordinal(), name);
            }
        }
        String[] imageTypeNameValues = new String[imageTypeNames.size()];
        for (Map.Entry<Integer, String> mapEntry : imageTypeNames.entrySet()) {
            int index = mapEntry.getKey();
            imageTypeNameValues[index] = mapEntry.getValue();
        }

        RenderedImage.ImageType defaultImageType = RenderedImage.ImageType.BlendedMSXRGBA8888Image;
        frameProcessor = new FrameProcessor(this, this, EnumSet.of(defaultImageType, RenderedImage.ImageType.ThermalRadiometricKelvinImage));

        ListView imageTypeListView = ((ListView) findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.imageTypeListView));
        imageTypeListView.setAdapter(new ArrayAdapter<>(this, com.flir.SoftheonExampleFlirOneApplication.R.layout.emptytextview, imageTypeNameValues));
        imageTypeListView.setSelection(defaultImageType.ordinal());
        imageTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (frameProcessor != null) {
                    RenderedImage.ImageType imageType = RenderedImage.ImageType.values()[position];
                    frameProcessor.setImageTypes(EnumSet.of(imageType, RenderedImage.ImageType.ThermalRadiometricKelvinImage));
                    if (imageType.isColorized()) {
                        findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.paletteListView).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.paletteListView).setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        imageTypeListView.setDivider(null);

        // Palette List View Setup
        ListView paletteListView = ((ListView) findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.paletteListView));
        paletteListView.setDivider(null);
        paletteListView.setAdapter(new ArrayAdapter<>(this, com.flir.SoftheonExampleFlirOneApplication.R.layout.emptytextview, RenderedImage.Palette.values()));
        paletteListView.setSelection(frameProcessor.getImagePalette().ordinal());
        paletteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (frameProcessor != null) {
                    frameProcessor.setImagePalette(RenderedImage.Palette.values()[position]);
                }
            }
        });
        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.

        mSystemUiHider = SystemUiHider.getInstance(this, contentView, mSystemUiHider.HIDER_FLAGS);
        mSystemUiHider.setup();

        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                            controlsViewTop.animate().translationY(visible ? 0 : -1 * mControlsHeight).setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                            controlsViewTop.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && !((ToggleButton) findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.change_view_button)).isChecked() && mSystemUiHider.AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(mSystemUiHider.AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSystemUiHider.TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.change_view_button).setOnTouchListener(mDelayHideTouchListener);


        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                int deviceRotation = orientation;
            }
        };
        mScaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Log.d("ZOOM", "zoom ongoing, scale: " + detector.getScaleFactor());
                frameProcessor.setMSXDistance(detector.getScaleFactor());
                return false;
            }
        });

        findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.fullscreen_content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);
                return true;
            }
        });

    }
    //endregion

    //region Device Delegate Methods

    /**
     * Called during device discovery, when a device is connected
     * During this callback, you should save a reference to device
     * You should also set the power update delegate for the device if you have one
     * Go ahead and start frame stream as soon as connected, in this use case
     * Finally we create a frame processor for rendering frames
     *
     * @param device - The Thermal Imaging Camera
     */
    @Override
    public void onDeviceConnected(Device device) {
        Log.i("SoftheonExampleApp", "Device connected!");

        //Remove the please connect view from the UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.pleaseConnect).setVisibility(View.GONE);
            }
        });

        //Set the Flir One Device as well as the power update and frame stream delegates to this activity
        flirOneDevice = device;
        flirOneDevice.setPowerUpdateDelegate(this);
        flirOneDevice.startFrameStream(this);

        //Get the toggle button
        final ToggleButton chargeCableButton = (ToggleButton) findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.chargeCableToggle);

        //If device is in simulated mode
        if (flirOneDevice instanceof SimulatedDevice) {
            //Update the UI to signal the charge cable is connected
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chargeCableButton.setChecked(chargeCableIsConnected);
                    chargeCableButton.setVisibility(View.VISIBLE);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chargeCableButton.setChecked(chargeCableIsConnected);
                    chargeCableButton.setVisibility(View.INVISIBLE);
                    findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.connect_sim_button).setEnabled(false);

                }
            });
        }

        //Enable the orientation event listener
        orientationEventListener.enable();
    }

    /**
     * Indicate to the user that the device has disconnected
     *
     * @param device - The Thermal Imaging Camera
     */
    @Override
    public void onDeviceDisconnected(Device device) {
        Log.i("SoftheonExampleApp", "Device disconnected!");

        //Get references to the text view, image view and charge cable button
        final ToggleButton chargeCableButton = (ToggleButton) findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.chargeCableToggle);
        final TextView levelTextView = (TextView) findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.batteryLevelTextView);
        final ImageView chargingIndicator = (ImageView) findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.batteryChargeIndicator);

        //Update the UI to signal that the device was disconnected
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.pleaseConnect).setVisibility(View.GONE);
                thermalImageView.setImageBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8));
                levelTextView.setText("--");
                chargeCableButton.setChecked(chargeCableIsConnected);
                chargeCableButton.setVisibility(View.INVISIBLE);
                chargingIndicator.setVisibility(View.GONE);
                thermalImageView.clearColorFilter();
                findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.tuningProgressBar).setVisibility(View.GONE);
                findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.tuningTextView).setVisibility(View.GONE);
                findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.connect_sim_button).setEnabled(true);
            }
        });

        //Remove the device reference and disable the device's orientation event listener
        flirOneDevice = null;
        orientationEventListener.disable();
    }

    /**
     * If using RenderedImage.ImageType.ThermalRadiometricKelvinImage, you should not rely on
     * the accuracy if tuningState is not Device.TuningState.Tuned
     *
     * @param tuningState - Represents the state of the flat-field calibration tuning
     */
    @Override
    public void onTuningStateChanged(Device.TuningState tuningState) {
        Log.i("SoftheonExampleApp", "Tuning state changed changed!");

        //Get the current tuning state from the device
        currentTuningState = tuningState;

        //If tuning state is in progress
        if (tuningState == Device.TuningState.InProgress) {
            //Update UI to show the tuning progress bar and tuning text view
            runOnUiThread(new Thread() {
                @Override
                public void run() {
                    super.run();
                    thermalImageView.setColorFilter(Color.DKGRAY, PorterDuff.Mode.DARKEN);
                    findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.tuningProgressBar).setVisibility(View.VISIBLE);
                    findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.tuningTextView).setVisibility(View.VISIBLE);
                }
            });
        } else {
            //Clear the color filter and remove the tuning progress bar and text view
            runOnUiThread(new Thread() {
                @Override
                public void run() {
                    super.run();
                    thermalImageView.clearColorFilter();
                    findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.tuningProgressBar).setVisibility(View.GONE);
                    findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.tuningTextView).setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * Called when the device confirms enabling or disabling the automatic tuning function.
     * @param deviceWillTuneAutomatically - The newly applied setting for automatic tuning. True if device will automatically tune.
     */
    @Override
    public void onAutomaticTuningChanged(boolean deviceWillTuneAutomatically) {

    }

    /**
     * Called whenever the battery charging state changes
     * @param batteryChargingState - the new state of the battery
     */
    @Override
    public void onBatteryChargingStateReceived(final Device.BatteryChargingState batteryChargingState) {
        Log.i("SoftheonExampleApp", "Battery charging state received!");

        //Update the battery indicator color based on the new battery state
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView chargingIndicator = (ImageView) findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.batteryChargeIndicator);
                if (originalChargingIndicatorColor == null) {
                    originalChargingIndicatorColor = chargingIndicator.getColorFilter();
                }
                switch (batteryChargingState) {
                    case FAULT:
                    case FAULT_HEAT:
                        chargingIndicator.setColorFilter(Color.RED);
                        chargingIndicator.setVisibility(View.VISIBLE);
                        break;
                    case FAULT_BAD_CHARGER:
                        chargingIndicator.setColorFilter(Color.DKGRAY);
                        chargingIndicator.setVisibility(View.VISIBLE);
                    case MANAGED_CHARGING:
                        chargingIndicator.setColorFilter(originalChargingIndicatorColor);
                        chargingIndicator.setVisibility(View.VISIBLE);
                        break;
                    case NO_CHARGING:
                    default:
                        chargingIndicator.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    /**
     * Called whenever the battery charge percentage changes
     * @param percentage - between 0 and 100 inclusive
     */
    @Override
    public void onBatteryPercentageReceived(final byte percentage) {
        Log.i("SoftheonExampleApp", "Battery percentage received!");

        //Get the battery level text view
        final TextView levelTextView = (TextView) findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.batteryLevelTextView);

        //Update the percentage of the battery level text view
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                levelTextView.setText(String.valueOf((int) percentage) + "%");
            }
        });
    }

    /**
     * Called when a frame has been received and processed.
     * Note: if you have requested N formats, this method will be called N times per Frame passed to the FrameProcessor instance.
     * If your goal is to record a video from a sequence of frames, use android.media.MediaCodec and call mediaCodec.queueInputBuffer
     * from this method with the frame's pixelData byte array put in the input ByteBuffer of the mediaCodec
     * @param renderedImage - The image after processing has been applied
     */
    @Override
    public void onFrameProcessed(final RenderedImage renderedImage) {
        if (renderedImage.imageType() == RenderedImage.ImageType.ThermalRadiometricKelvinImage) {
            CalculateSpotMeterValue(renderedImage);
        } else {
            if (thermalBitmap == null) {
                thermalBitmap = renderedImage.getBitmap();
            } else {
                try {
                    renderedImage.copyToBitmap(thermalBitmap);
                } catch (IllegalArgumentException e) {
                    thermalBitmap = renderedImage.getBitmap();
                }
            }
            updateThermalImageView(thermalBitmap);
        }

        /*
        Capture this image if requested.
        */
        if (this.imageCaptureRequested) {
            CaptureImage(renderedImage);
        }
    }

    /**
     * Called when a frame has been received when streaming
     * @param frame - the object representing a received frame
     */
    @Override
    public void onFrameReceived(Frame frame) {
        Log.v("SoftheonExampleApp", "Frame received!");

        if (currentTuningState != Device.TuningState.InProgress) {
            frameProcessor.processFrame(frame);
        }
    }

//endregion

    //region UI Event Listeners

    /**
     * Event listener for tune button click
     * @param v - The view
     */
    public void onTuneClicked(View v) {
        if (flirOneDevice != null) {
            flirOneDevice.performTuning();
        }
    }

    /**
     * Event listener for capture image
     * @param v - The view
     */
    public void onCaptureImageClicked(View v) {
        // if nothing's connected, let's load an image instead?
        if (flirOneDevice == null && lastSavedPath != null) {
            // load!
            File file = new File(lastSavedPath);


            LoadedFrame frame = new LoadedFrame(file);

            // load the frame
            onFrameReceived(frame);
        } else {
            this.imageCaptureRequested = true;
        }
    }

    /**
     * Event listener for toggle simulator
     * @param v - The view
     */
    public void onConnectSimClicked(View v) {
        if (flirOneDevice == null) {
            try {
                flirOneDevice = new SimulatedDevice(this, this, getResources().openRawResource(com.flir.SoftheonExampleFlirOneApplication.R.raw.sampleframes), 10);
                flirOneDevice.setPowerUpdateDelegate(this);
                chargeCableIsConnected = true;
            } catch (Exception ex) {
                flirOneDevice = null;
                Log.w("FLIROneExampleApp", "IO EXCEPTION");
                ex.printStackTrace();
            }
        } else if (flirOneDevice instanceof SimulatedDevice) {
            flirOneDevice.close();
            flirOneDevice = null;
        }
    }

    /**
     * Event listener for simulated charge cable clicked
     * @param v - The view
     */
    public void onSimulatedChargeCableToggleClicked(View v) {
        if (flirOneDevice instanceof SimulatedDevice) {
            chargeCableIsConnected = !chargeCableIsConnected;
            ((SimulatedDevice) flirOneDevice).setChargeCableState(chargeCableIsConnected);
        }
    }

    /**
     * Event listener for rotate button
     * @param v - The view
     */
    public void onRotateClicked(View v) {
        ToggleButton theSwitch = (ToggleButton) v;
        if (theSwitch.isChecked()) {
            thermalImageView.setRotation(180);
        } else {
            thermalImageView.setRotation(0);
        }
    }

    /**
     * Event listener for change thermal image view
     * @param v - The view
     */
    public void onChangeViewClicked(View v) {
        if (frameProcessor == null) {
            ((ToggleButton) v).setChecked(false);
            return;
        }
        ListView paletteListView = (ListView) findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.paletteListView);
        ListView imageTypeListView = (ListView) findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.imageTypeListView);
        if (((ToggleButton) v).isChecked()) {
            // only show palette list if selected image type is colorized
            paletteListView.setVisibility(View.INVISIBLE);
            for (RenderedImage.ImageType imageType : frameProcessor.getImageTypes()) {
                if (imageType.isColorized()) {
                    paletteListView.setVisibility(View.VISIBLE);
                    break;
                }
            }
            imageTypeListView.setVisibility(View.VISIBLE);
            findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.imageTypeListContainer).setVisibility(View.VISIBLE);
        } else {
            findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.imageTypeListContainer).setVisibility(View.GONE);
        }


    }

    /**
     * Event listener for image type drop down list
     * @param v - The view
     */
    public void onImageTypeListViewClicked(View v) {
        int index = ((ListView) v).getSelectedItemPosition();
        RenderedImage.ImageType imageType = RenderedImage.ImageType.values()[index];
        frameProcessor.setImageTypes(EnumSet.of(imageType, RenderedImage.ImageType.ThermalRadiometricKelvinImage));
        int paletteVisibility = (imageType.isColorized()) ? View.VISIBLE : View.GONE;
        findViewById(com.flir.SoftheonExampleFlirOneApplication.R.id.paletteListView).setVisibility(paletteVisibility);
    }

    /**
     * Event listener for Palette List View
     * @param v - The view
     */
    public void onPaletteListViewClicked(View v) {
        RenderedImage.Palette pal = (RenderedImage.Palette) (((ListView) v).getSelectedItem());
        frameProcessor.setImagePalette(pal);
    }

    /**
     * On Pause event listener
     */
    @Override
    public void onPause() {
        super.onPause();
        if (flirOneDevice != null) {
            flirOneDevice.stopFrameStream();
        }
    }

    /**
     * On Resume event listener
     */
    @Override
    public void onResume() {
        super.onResume();
        if (flirOneDevice != null) {
            flirOneDevice.startFrameStream(this);
        }
    }

    /**
     * On Stop event listener
     */
    @Override
    public void onStop() {
        // We must unregister our usb receiver, otherwise we will steal events from other apps
        Log.e("PreviewActivity", "onStop, stopping discovery!");
        Device.stopDiscovery();
        flirOneDevice = null;
        super.onStop();
    }

    /**
     * On post create event listener
     * @param savedInstanceState - The saved instance state
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (mSystemUiHider.AUTO_HIDE) {
                delayedHide(mSystemUiHider.AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    //endregion

    //region Private Methods

    /**
     * Calculates the spot meter value using the Thermal Radiometric Kelvin Image
     * @param renderedImage - The rendered image
     */
    private void CalculateSpotMeterValue(RenderedImage renderedImage) {
        // Note: this code is not optimized

        int[] thermalPixels = renderedImage.thermalPixelValues();

        // average the center 9 pixels for the spot meter
        int width = renderedImage.width();
        int height = renderedImage.height();
        int centerPixelIndex = width * (height / 2) + (width / 2);
        int[] centerPixelIndexes = new int[]{
                centerPixelIndex, centerPixelIndex - 1, centerPixelIndex + 1,
                centerPixelIndex - width,
                centerPixelIndex - width - 1,
                centerPixelIndex - width + 1,
                centerPixelIndex + width,
                centerPixelIndex + width - 1,
                centerPixelIndex + width + 1
        };

        double averageTemp = 0;

        for (int i = 0; i < centerPixelIndexes.length; i++) {
            // Remember: all primitives are signed, we want the unsigned value,
            // we've used renderedImage.thermalPixelValues() to get unsigned values
            int pixelValue = (thermalPixels[centerPixelIndexes[i]]);
            averageTemp += (((double) pixelValue) - averageTemp) / ((double) i + 1);
        }
        double averageC = (averageTemp / 100) - 273.15;
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        final String spotMeterValue = numberFormat.format(averageC) + "ºC";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.spotMeterValue)).setText(spotMeterValue);
            }
        });

        // if radiometric is the only type, also show the image
        if (frameProcessor.getImageTypes().size() == 1) {
            // example of a custom colorization, maps temperatures 0-100C to 8-bit gray-scale
            byte[] argbPixels = new byte[width * height * 4];
            final byte aPixValue = (byte) 255;
            for (int p = 0; p < thermalPixels.length; p++) {
                int destP = p * 4;
                byte pixValue = (byte) (Math.min(0xff, Math.max(0x00, (thermalPixels[p] - 27315) * (255.0 / 10000.0))));

                argbPixels[destP + 3] = aPixValue;
                // red pixel
                argbPixels[destP] = argbPixels[destP + 1] = argbPixels[destP + 2] = pixValue;
            }
            final Bitmap demoBitmap = Bitmap.createBitmap(width, renderedImage.height(), Bitmap.Config.ARGB_8888);

            demoBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(argbPixels));

            updateThermalImageView(demoBitmap);
        }
    }

    /**
     * Captures the image
     * @param renderedImage - The rendered image
     */
    private void CaptureImage(final RenderedImage renderedImage) {
        imageCaptureRequested = false;
        final Context context = this;
        new Thread(new Runnable() {
            public void run() {
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ssZ", Locale.getDefault());
                String formatedDate = sdf.format(new Date());
                String fileName = "FLIROne-" + formatedDate + ".jpg";
                try {
                    lastSavedPath = path + "/" + fileName;
                    renderedImage.getFrame().save(new File(lastSavedPath), RenderedImage.Palette.Iron, RenderedImage.ImageType.BlendedMSXRGBA8888Image);

                    MediaScannerConnection.scanFile(context,
                            new String[]{path + "/" + fileName}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> uri=" + uri);
                                }

                            });

                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        thermalImageView.animate().setDuration(50).scaleY(0).withEndAction((new Runnable() {
                            public void run() {
                                thermalImageView.animate().setDuration(50).scaleY(1);
                            }
                        }));
                    }
                });
            }
        }).start();
    }

    /**
     * Updates the thermal image view's bitmap image
     * @param frame - The bitmap image to use
     */
    private void updateThermalImageView(final Bitmap frame) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                thermalImageView.setImageBitmap(frame);
            }
        });
    }

    //endregion
}
