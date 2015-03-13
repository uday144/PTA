package com.ptapp.bean;

/**
 * This class is our model and contains the data we will save in the database
 * and show in the user interface.
 */
public class MessageBean {
	// private variables
	private long id;
	private String parentId;
	private String educatorId;
	private String studentId;
	private String folderId;
	private String needPush;
	private String msgTxt;
	private String mediaUrl;
	private String mediaLocalPath;
	private String mediaMimeType;
	private String mediaPTAppType;
	private String mediaSize;
	private String mediaName;
	private String thumbImage;
	private String thumbServerPath;
	private String thumbLocalPath;
	private String receivedTimestamp;
	private String sendTimestamp;
	private String reciptServerTimestamp;
	private String receiptDeviceTimestamp;

	// Empty constructor
	public MessageBean() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getEducatorId() {
		return educatorId;
	}

	public void setEducatorId(String educatorId) {
		this.educatorId = educatorId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getNeedPush() {
		return needPush;
	}

	public void setNeedPush(String needPush) {
		this.needPush = needPush;
	}

	public String getMsgTxt() {
		return msgTxt;
	}

	public void setMsgTxt(String msgTxt) {
		this.msgTxt = msgTxt;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public String getMediaLocalPath() {
		return mediaLocalPath;
	}

	public void setMediaLocalPath(String mediaLocalPath) {
		this.mediaLocalPath = mediaLocalPath;
	}

	public String getMediaMimeType() {
		return mediaMimeType;
	}

	public void setMediaMimeType(String mediaMimeType) {
		this.mediaMimeType = mediaMimeType;
	}

	public String getMediaPTAppType() {
		return mediaPTAppType;
	}

	public void setMediaPTAppType(String mediaPTAppType) {
		this.mediaPTAppType = mediaPTAppType;
	}

	public String getMediaSize() {
		return mediaSize;
	}

	public void setMediaSize(String mediaSize) {
		this.mediaSize = mediaSize;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getThumbImage() {
		return thumbImage;
	}

	public void setThumbImage(String thumbImage) {
		this.thumbImage = thumbImage;
	}

	public String getThumbServerPath() {
		return thumbServerPath;
	}

	public void setThumbServerPath(String thumbServerPath) {
		this.thumbServerPath = thumbServerPath;
	}

	public String getThumbLocalPath() {
		return thumbLocalPath;
	}

	public void setThumbLocalPath(String thumbLocalPath) {
		this.thumbLocalPath = thumbLocalPath;
	}

	public String getReceivedTimestamp() {
		return receivedTimestamp;
	}

	public void setReceivedTimestamp(String receivedTimestamp) {
		this.receivedTimestamp = receivedTimestamp;
	}

	public String getSendTimestamp() {
		return sendTimestamp;
	}

	public void setSendTimestamp(String sendTimestamp) {
		this.sendTimestamp = sendTimestamp;
	}

	public String getReciptServerTimestamp() {
		return reciptServerTimestamp;
	}

	public void setReciptServerTimestamp(String reciptServerTimestamp) {
		this.reciptServerTimestamp = reciptServerTimestamp;
	}

	public String getReceiptDeviceTimestamp() {
		return receiptDeviceTimestamp;
	}

	public void setReceiptDeviceTimestamp(String receiptDeviceTimestamp) {
		this.receiptDeviceTimestamp = receiptDeviceTimestamp;
	}

}
