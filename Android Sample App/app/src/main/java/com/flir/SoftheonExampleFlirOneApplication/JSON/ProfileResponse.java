package com.flir.SoftheonExampleFlirOneApplication.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Acl",
        "Dates",
        "Doubles",
        "ExtensionCount",
        "Integers",
        "ModificationTime",
        "ModifierUserID",
        "OwnerGroupID",
        "OwnerUserID",
        "ProfileID",
        "Strings",
        "Type"
})
public class ProfileResponse {

    @JsonProperty("Acl")
    private Integer acl;
    @JsonProperty("Dates")
    private List<String> dates = null;
    @JsonProperty("Doubles")
    private List<Double> doubles = null;
    @JsonProperty("ExtensionCount")
    private Integer extensionCount;
    @JsonProperty("Integers")
    private List<Integer> integers = null;
    @JsonProperty("ModificationTime")
    private String modificationTime;
    @JsonProperty("ModifierUserID")
    private Integer modifierUserID;
    @JsonProperty("OwnerGroupID")
    private Integer ownerGroupID;
    @JsonProperty("OwnerUserID")
    private Integer ownerUserID;
    @JsonProperty("ProfileID")
    private Integer profileID;
    @JsonProperty("Strings")
    private List<String> strings = null;
    @JsonProperty("Type")
    private Integer type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public ProfileResponse() {
    }

    /**
     *
     * @param ownerGroupID
     * @param integers
     * @param profileID
     * @param dates
     * @param acl
     * @param strings
     * @param doubles
     * @param modifierUserID
     * @param modificationTime
     * @param type
     * @param ownerUserID
     * @param extensionCount
     */
    public ProfileResponse(Integer acl, List<String> dates, List<Double> doubles, Integer extensionCount, List<Integer> integers, String modificationTime, Integer modifierUserID, Integer ownerGroupID, Integer ownerUserID, Integer profileID, List<String> strings, Integer type) {
        super();
        this.acl = acl;
        this.dates = dates;
        this.doubles = doubles;
        this.extensionCount = extensionCount;
        this.integers = integers;
        this.modificationTime = modificationTime;
        this.modifierUserID = modifierUserID;
        this.ownerGroupID = ownerGroupID;
        this.ownerUserID = ownerUserID;
        this.profileID = profileID;
        this.strings = strings;
        this.type = type;
    }

    @JsonProperty("Acl")
    public Integer getAcl() {
        return acl;
    }

    @JsonProperty("Acl")
    public void setAcl(Integer acl) {
        this.acl = acl;
    }

    @JsonProperty("Dates")
    public List<String> getDates() {
        return dates;
    }

    @JsonProperty("Dates")
    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    @JsonProperty("Doubles")
    public List<Double> getDoubles() {
        return doubles;
    }

    @JsonProperty("Doubles")
    public void setDoubles(List<Double> doubles) {
        this.doubles = doubles;
    }

    @JsonProperty("ExtensionCount")
    public Integer getExtensionCount() {
        return extensionCount;
    }

    @JsonProperty("ExtensionCount")
    public void setExtensionCount(Integer extensionCount) {
        this.extensionCount = extensionCount;
    }

    @JsonProperty("Integers")
    public List<Integer> getIntegers() {
        return integers;
    }

    @JsonProperty("Integers")
    public void setIntegers(List<Integer> integers) {
        this.integers = integers;
    }

    @JsonProperty("ModificationTime")
    public String getModificationTime() {
        return modificationTime;
    }

    @JsonProperty("ModificationTime")
    public void setModificationTime(String modificationTime) {
        this.modificationTime = modificationTime;
    }

    @JsonProperty("ModifierUserID")
    public Integer getModifierUserID() {
        return modifierUserID;
    }

    @JsonProperty("ModifierUserID")
    public void setModifierUserID(Integer modifierUserID) {
        this.modifierUserID = modifierUserID;
    }

    @JsonProperty("OwnerGroupID")
    public Integer getOwnerGroupID() {
        return ownerGroupID;
    }

    @JsonProperty("OwnerGroupID")
    public void setOwnerGroupID(Integer ownerGroupID) {
        this.ownerGroupID = ownerGroupID;
    }

    @JsonProperty("OwnerUserID")
    public Integer getOwnerUserID() {
        return ownerUserID;
    }

    @JsonProperty("OwnerUserID")
    public void setOwnerUserID(Integer ownerUserID) {
        this.ownerUserID = ownerUserID;
    }

    @JsonProperty("ProfileID")
    public Integer getProfileID() {
        return profileID;
    }

    @JsonProperty("ProfileID")
    public void setProfileID(Integer profileID) {
        this.profileID = profileID;
    }

    @JsonProperty("Strings")
    public List<String> getStrings() {
        return strings;
    }

    @JsonProperty("Strings")
    public void setStrings(List<String> strings) {
        this.strings = strings;
    }

    @JsonProperty("Type")
    public Integer getType() {
        return type;
    }

    @JsonProperty("Type")
    public void setType(Integer type) {
        this.type = type;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
