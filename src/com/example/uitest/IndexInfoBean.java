package com.example.uitest;

import android.graphics.Bitmap;
import utils.json.JSONArray;

/**
 * @author Administrator 谭林
 *         主页上应该显示的所有信息，方便处理，将服务器得到的数据封装成一个对象
 *         <p/>
 *         这与显示需要的数据（bitmap资源）不一样
 *         <p/>
 *         Created by Administrator on 2014/12/11.
 */
public class IndexInfoBean {

	private int id;

	private int rs_id;

	private int sharesNumber;

	private int commentNumber;

	private int likeNumber;

	private String myWords;

	//	private Timestamp time;

	private String time;

	private String album;

	private String viewPhoto;

	private String detailPhoto;

	private String hasDetail;

	private String isLocated;


	//	private JSONArray location;


	private String photoClass;

	private JSONArray photoAt;

	private JSONArray photoTopic;

	private String olderWords;

	private Bitmap photoHead;

	private Bitmap pictureShow;

	private String location;

	public String getLocation() {

		return location;
	}

	public void setLocation(String location) {

		this.location = location;
	}

	public void setPhotoHead(Bitmap photoHead) {

		this.photoHead = photoHead;
	}


	public Bitmap getPhotoHead() {

		return photoHead;
	}


	public Bitmap getPictureShow() {

		return pictureShow;
	}

	public void setPictureShow(Bitmap pictureShow) {

		this.pictureShow = pictureShow;
	}

	public int getId() {

		return id;
	}

	public void setId(int id) {

		this.id = id;
	}

	public int getRs_id() {

		return rs_id;
	}

	public void setRs_id(int rs_id) {

		this.rs_id = rs_id;
	}

	public int getSharesNumber() {

		return sharesNumber;
	}

	public void setSharesNumber(int sharesNumber) {

		this.sharesNumber = sharesNumber;
	}

	public int getCommentNumber() {

		return commentNumber;
	}

	public void setCommentNumber(int commentNumber) {

		this.commentNumber = commentNumber;
	}

	public int getLikeNumber() {

		return likeNumber;
	}

	public void setLikeNumber(int likeNumber) {

		this.likeNumber = likeNumber;
	}

	public String getMyWords() {

		return myWords;
	}

	public void setMyWords(String myWords) {

		this.myWords = myWords;
	}

	public String getTime() {

		return time;
	}

	public void setTime(String time) {

		this.time = time;
	}

	public String getAlbum() {

		return album;
	}

	public void setAlbum(String album) {

		this.album = album;
	}

	public String getViewPhoto() {

		return viewPhoto;
	}

	public void setViewPhoto(String viewPhoto) {

		this.viewPhoto = viewPhoto;
	}

	public String getDetailPhoto() {

		return detailPhoto;
	}

	public void setDetailPhoto(String detailPhoto) {

		this.detailPhoto = detailPhoto;
	}

	public String getHasDetail() {

		return hasDetail;
	}

	public void setHasDetail(String hasDetail) {

		this.hasDetail = hasDetail;
	}

	public String getIsLocated() {

		return isLocated;
	}

	public void setIsLocated(String isLocated) {

		this.isLocated = isLocated;
	}

	//	public JSONArray getLocation() {
	//
	//		return location;
	//	}
	//
	//	public void setLocation(JSONArray location) {
	//
	//		this.location = location;
	//	}

	public String getPhotoClass() {

		return photoClass;
	}

	public void setPhotoClass(String photoClass) {

		this.photoClass = photoClass;
	}

	public JSONArray getPhotoAt() {

		return photoAt;
	}

	public void setPhotoAt(JSONArray photoAt) {

		this.photoAt = photoAt;
	}

	public JSONArray getPhotoTopic() {

		return photoTopic;
	}

	public void setPhotoTopic(JSONArray photoTopic) {

		this.photoTopic = photoTopic;
	}

	public String getOlderWords() {

		return olderWords;
	}

	public void setOlderWords(String olderWords) {

		this.olderWords = olderWords;
	}


}
