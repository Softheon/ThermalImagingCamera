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
        "AccessCount",
        "AccessTime",
        "Acl",
        "Age",
        "AttachCount",
        "Category",
        "CreationTime",
        "CreatorGroupID",
        "CreatorUserID",
        "FolderID",
        "ModificationTime",
        "ModifierGroupID",
        "ModifierUserID",
        "Name",
        "NoteCount",
        "OwnerGroupID",
        "OwnerUserID",
        "ProfileCount",
        "Profiles",
        "ReferenceCount",
        "ReservedInt1",
        "ReservedInt2",
        "ReservedString1",
        "ResolutionTime",
        "State",
        "Subtype",
        "Type"
})
public class ThermalImageReadingResponse {

    @JsonProperty("AccessCount")
    private Integer accessCount;
    @JsonProperty("AccessTime")
    private String accessTime;
    @JsonProperty("Acl")
    private Integer acl;
    @JsonProperty("Age")
    private Integer age;
    @JsonProperty("AttachCount")
    private Integer attachCount;
    @JsonProperty("Category")
    private Integer category;
    @JsonProperty("CreationTime")
    private String creationTime;
    @JsonProperty("CreatorGroupID")
    private Integer creatorGroupID;
    @JsonProperty("CreatorUserID")
    private Integer creatorUserID;
    @JsonProperty("FolderID")
    private Integer folderID;
    @JsonProperty("ModificationTime")
    private String modificationTime;
    @JsonProperty("ModifierGroupID")
    private Integer modifierGroupID;
    @JsonProperty("ModifierUserID")
    private Integer modifierUserID;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("NoteCount")
    private Integer noteCount;
    @JsonProperty("OwnerGroupID")
    private Integer ownerGroupID;
    @JsonProperty("OwnerUserID")
    private Integer ownerUserID;
    @JsonProperty("ProfileCount")
    private Integer profileCount;
    @JsonProperty("Profiles")
    private List<ProfileResponse> profiles = null;
    @JsonProperty("ReferenceCount")
    private Integer referenceCount;
    @JsonProperty("ReservedInt1")
    private Integer reservedInt1;
    @JsonProperty("ReservedInt2")
    private Integer reservedInt2;
    @JsonProperty("ReservedString1")
    private String reservedString1;
    @JsonProperty("ResolutionTime")
    private String resolutionTime;
    @JsonProperty("State")
    private Integer state;
    @JsonProperty("Subtype")
    private Integer subtype;
    @JsonProperty("Type")
    private Integer type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public ThermalImageReadingResponse() {
    }

    /**
     *
     * @param accessTime
     * @param reservedString1
     * @param noteCount
     * @param reservedInt1
     * @param reservedInt2
     * @param state
     * @param type
     * @param profileCount
     * @param ownerGroupID
     * @param age
     * @param name
     * @param referenceCount
     * @param creationTime
     * @param resolutionTime
     * @param acl
     * @param modificationTime
     * @param folderID
     * @param ownerUserID
     * @param category
     * @param creatorGroupID
     * @param modifierGroupID
     * @param subtype
     * @param attachCount
     * @param accessCount
     * @param modifierUserID
     * @param profiles
     * @param creatorUserID
     */
    public ThermalImageReadingResponse(Integer accessCount, String accessTime, Integer acl, Integer age, Integer attachCount, Integer category, String creationTime, Integer creatorGroupID, Integer creatorUserID, Integer folderID, String modificationTime, Integer modifierGroupID, Integer modifierUserID, String name, Integer noteCount, Integer ownerGroupID, Integer ownerUserID, Integer profileCount, List<ProfileResponse> profiles, Integer referenceCount, Integer reservedInt1, Integer reservedInt2, String reservedString1, String resolutionTime, Integer state, Integer subtype, Integer type) {
        super();
        this.accessCount = accessCount;
        this.accessTime = accessTime;
        this.acl = acl;
        this.age = age;
        this.attachCount = attachCount;
        this.category = category;
        this.creationTime = creationTime;
        this.creatorGroupID = creatorGroupID;
        this.creatorUserID = creatorUserID;
        this.folderID = folderID;
        this.modificationTime = modificationTime;
        this.modifierGroupID = modifierGroupID;
        this.modifierUserID = modifierUserID;
        this.name = name;
        this.noteCount = noteCount;
        this.ownerGroupID = ownerGroupID;
        this.ownerUserID = ownerUserID;
        this.profileCount = profileCount;
        this.profiles = profiles;
        this.referenceCount = referenceCount;
        this.reservedInt1 = reservedInt1;
        this.reservedInt2 = reservedInt2;
        this.reservedString1 = reservedString1;
        this.resolutionTime = resolutionTime;
        this.state = state;
        this.subtype = subtype;
        this.type = type;
    }

    @JsonProperty("AccessCount")
    public Integer getAccessCount() {
        return accessCount;
    }

    @JsonProperty("AccessCount")
    public void setAccessCount(Integer accessCount) {
        this.accessCount = accessCount;
    }

    @JsonProperty("AccessTime")
    public String getAccessTime() {
        return accessTime;
    }

    @JsonProperty("AccessTime")
    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    @JsonProperty("Acl")
    public Integer getAcl() {
        return acl;
    }

    @JsonProperty("Acl")
    public void setAcl(Integer acl) {
        this.acl = acl;
    }

    @JsonProperty("Age")
    public Integer getAge() {
        return age;
    }

    @JsonProperty("Age")
    public void setAge(Integer age) {
        this.age = age;
    }

    @JsonProperty("AttachCount")
    public Integer getAttachCount() {
        return attachCount;
    }

    @JsonProperty("AttachCount")
    public void setAttachCount(Integer attachCount) {
        this.attachCount = attachCount;
    }

    @JsonProperty("Category")
    public Integer getCategory() {
        return category;
    }

    @JsonProperty("Category")
    public void setCategory(Integer category) {
        this.category = category;
    }

    @JsonProperty("CreationTime")
    public String getCreationTime() {
        return creationTime;
    }

    @JsonProperty("CreationTime")
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    @JsonProperty("CreatorGroupID")
    public Integer getCreatorGroupID() {
        return creatorGroupID;
    }

    @JsonProperty("CreatorGroupID")
    public void setCreatorGroupID(Integer creatorGroupID) {
        this.creatorGroupID = creatorGroupID;
    }

    @JsonProperty("CreatorUserID")
    public Integer getCreatorUserID() {
        return creatorUserID;
    }

    @JsonProperty("CreatorUserID")
    public void setCreatorUserID(Integer creatorUserID) {
        this.creatorUserID = creatorUserID;
    }

    @JsonProperty("FolderID")
    public Integer getFolderID() {
        return folderID;
    }

    @JsonProperty("FolderID")
    public void setFolderID(Integer folderID) {
        this.folderID = folderID;
    }

    @JsonProperty("ModificationTime")
    public String getModificationTime() {
        return modificationTime;
    }

    @JsonProperty("ModificationTime")
    public void setModificationTime(String modificationTime) {
        this.modificationTime = modificationTime;
    }

    @JsonProperty("ModifierGroupID")
    public Integer getModifierGroupID() {
        return modifierGroupID;
    }

    @JsonProperty("ModifierGroupID")
    public void setModifierGroupID(Integer modifierGroupID) {
        this.modifierGroupID = modifierGroupID;
    }

    @JsonProperty("ModifierUserID")
    public Integer getModifierUserID() {
        return modifierUserID;
    }

    @JsonProperty("ModifierUserID")
    public void setModifierUserID(Integer modifierUserID) {
        this.modifierUserID = modifierUserID;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("NoteCount")
    public Integer getNoteCount() {
        return noteCount;
    }

    @JsonProperty("NoteCount")
    public void setNoteCount(Integer noteCount) {
        this.noteCount = noteCount;
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

    @JsonProperty("ProfileCount")
    public Integer getProfileCount() {
        return profileCount;
    }

    @JsonProperty("ProfileCount")
    public void setProfileCount(Integer profileCount) {
        this.profileCount = profileCount;
    }

    @JsonProperty("Profiles")
    public List<ProfileResponse> getProfiles() {
        return profiles;
    }

    @JsonProperty("Profiles")
    public void setProfiles(List<ProfileResponse> profiles) {
        this.profiles = profiles;
    }

    @JsonProperty("ReferenceCount")
    public Integer getReferenceCount() {
        return referenceCount;
    }

    @JsonProperty("ReferenceCount")
    public void setReferenceCount(Integer referenceCount) {
        this.referenceCount = referenceCount;
    }

    @JsonProperty("ReservedInt1")
    public Integer getReservedInt1() {
        return reservedInt1;
    }

    @JsonProperty("ReservedInt1")
    public void setReservedInt1(Integer reservedInt1) {
        this.reservedInt1 = reservedInt1;
    }

    @JsonProperty("ReservedInt2")
    public Integer getReservedInt2() {
        return reservedInt2;
    }

    @JsonProperty("ReservedInt2")
    public void setReservedInt2(Integer reservedInt2) {
        this.reservedInt2 = reservedInt2;
    }

    @JsonProperty("ReservedString1")
    public String getReservedString1() {
        return reservedString1;
    }

    @JsonProperty("ReservedString1")
    public void setReservedString1(String reservedString1) {
        this.reservedString1 = reservedString1;
    }

    @JsonProperty("ResolutionTime")
    public String getResolutionTime() {
        return resolutionTime;
    }

    @JsonProperty("ResolutionTime")
    public void setResolutionTime(String resolutionTime) {
        this.resolutionTime = resolutionTime;
    }

    @JsonProperty("State")
    public Integer getState() {
        return state;
    }

    @JsonProperty("State")
    public void setState(Integer state) {
        this.state = state;
    }

    @JsonProperty("Subtype")
    public Integer getSubtype() {
        return subtype;
    }

    @JsonProperty("Subtype")
    public void setSubtype(Integer subtype) {
        this.subtype = subtype;
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