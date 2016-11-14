//package com.hd.wlj.duohaowan.util;
//
//import android.graphics.Rect;
//
//import java.awt.Rectangle;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.net.URL;
//
//import javax.imageio.ImageIO;
//
///**
// * 艺术作品
// * @author lymava
// *
// */
//public class Aartworks {
//
//	public static void main(String[] args) throws Exception {
//		Aartworks artworks = new Aartworks();
//
//		URL resource = Aartworks.class.getClass().getResource("/file");
//
//
//		System.out.println(resource.getPath()+"/zuopin.jpg");
//
//
//		File artworks_pic_file = new File(resource.getPath()+"/zuopin.jpg");
//		File artworks_huakuang_file = new File(resource.getPath()+"/huakuang.jpg");
//		File artworks_beijing_file = new File(resource.getPath()+"/背景.jpg");
//		File artworks_kazhi1_file = new File(resource.getPath()+"/黄.jpg");
//		File artworks_kazhi2_file = new File(resource.getPath()+"/灰.jpg");
//
//		Double artwork_bili = 0.8;
//		Double kazhi1_bili = 0.9;
//		Double kazhi2_bili = 0.95;
//
//
//		BufferedImage artworks_pic_bufferedImage = ImageIO.read(artworks_pic_file);
//		BufferedImage artworks_huakuang_bufferedImage = ImageIO.read(artworks_huakuang_file);
//		BufferedImage artworks_beijing_bufferedImage = ImageIO.read(artworks_beijing_file);
//		BufferedImage artworks_kazhi1_bufferedImage = ImageIO.read(artworks_kazhi1_file);
//		BufferedImage artworks_kazhi2_bufferedImage = ImageIO.read(artworks_kazhi2_file);
//
//		Rectangle searchRectangleOne_old = ImageUtil.searchRectangleOne(artworks_huakuang_bufferedImage);
//
//		Rectangle backgroundWall_position = ImageUtil.searchRectangleOne(artworks_beijing_bufferedImage);
//
//		Rectangle artwork_rectangle = ImageUtil.resizeRectangle(searchRectangleOne_old, artwork_bili);
//		Rectangle kazhi1_rectangle = ImageUtil.resizeRectangle(searchRectangleOne_old, kazhi1_bili);
//		Rectangle kazhi2_rectangle = ImageUtil.resizeRectangle(searchRectangleOne_old, kazhi2_bili);
//
//		long currentTimeMillis = System.currentTimeMillis();
//
//
//		artworks.setArtworks_pic(artworks_pic_bufferedImage);
//		artworks.setBackgroundWall(artworks_beijing_bufferedImage);
//		artworks.setHuakuang(artworks_huakuang_bufferedImage);
//		artworks.setKazhi_1(artworks_kazhi1_bufferedImage);
//		artworks.setKazhi_2(artworks_kazhi2_bufferedImage);
//
//		artworks.setArtworks_rectangle(artwork_rectangle);
//		artworks.setKazhi_1_position(kazhi1_rectangle);
//		artworks.setKazhi_2_position(kazhi2_rectangle);
//		artworks.setBackgroundWall_position(backgroundWall_position);
//
//		BufferedImage finalBufferedImage = artworks.getFinalBufferedImage();
//
//		System.out.println(System.currentTimeMillis()-currentTimeMillis);
//
//		FileOutputStream fos = new FileOutputStream(resource.getPath()+"/../../src/file/out.jpg");
//
//		ImageUtil.write(finalBufferedImage, fos);
//
//		System.out.println(System.currentTimeMillis()-currentTimeMillis);
//		System.exit(0);
//	}
//
//	public BufferedImage getFinalBufferedImage(){
//
//		if(artworks_pic == null || artworks_rectangle == null){
//			return null;
//		}
//
//		BufferedImage finalBufferedImage = this.getArtworks_pic();
//
//
//		if(huakuang != null){
//
//			if(kazhi_2_position != null && kazhi_2 != null){
//				huakuang = ImageUtil.imageSynthesis(huakuang, kazhi_2,kazhi_2_position);
//			}
//
//			if(kazhi_1_position != null && kazhi_1 != null){
//				huakuang = ImageUtil.imageSynthesis(huakuang, kazhi_1,kazhi_1_position);
//			}
//
//			huakuang = ImageUtil.imageSynthesis(huakuang, artworks_pic,artworks_rectangle);
//
//			finalBufferedImage = huakuang;
//		}
//		if(backgroundWall != null && backgroundWall_position != null){
//			finalBufferedImage = ImageUtil.imageSynthesis(backgroundWall, finalBufferedImage,backgroundWall_position);
//		}
//
//		return finalBufferedImage;
//	}
//
//	/**
//	 * 作者
//	 */
//	private String zuozhe;
//	/**
//	 * 年份
//	 */
//	private String years;
//	/**
//	 * 朝代
//	 */
//	private String chaodai;
//	 /**
//	  * 风格
//	  */
//	private String fengge;
//
//	/**
//	 * 作品的图像
//	 */
//	private BufferedImage artworks_pic;
//	/**
//	 * 作品的位置
//	 * 当有画框的时候 这个是 作品在画框中的位置
//	 * 当没有画框有背景的时候 这个是 作品在作品中的位置
//	 * 当既没有画框又没有背景的时候这个失效
//	 */
//	private Rectangle artworks_rectangle;
//	/**
//	 * 图片宽度
//	 */
//	private Integer pic_width;
//	/**
//	 * 图片高度
//	 */
//	private Integer pic_height;
//	 /**
//	  * 真实尺寸 宽 单位cm
//	  */
//	private Integer true_width;
//	 /**
//	  * 真实尺寸 高 单位cm
//	  */
//	private Integer true_height;
//	/**
//	 * 背景
//	 */
//	private BufferedImage backgroundWall;
//	/**
//	 * 背景作品框的位置
//	 */
//	private Rectangle backgroundWall_position;
//	/**
//	 * 画框
//	 */
//	private BufferedImage huakuang;
//	/**
//	 * 卡纸1
//	 */
//	private BufferedImage kazhi_1;
//	/**
//	 * 卡纸1位置
//	 */
//	private Rectangle kazhi_1_position;
//	/**
//	 * 卡纸2
//	 */
//	private BufferedImage kazhi_2;
//	/**
//	 * 卡纸2系统编号
//	 */
//	private String kazhi_2_id;
//	/**
//	 * 卡纸1位置
//	 */
//	private Rectangle kazhi_2_position;
//
//	public Rectangle getBackgroundWall_position() {
//		return backgroundWall_position;
//	}
//
//	public void setBackgroundWall_position(Rectangle backgroundWall_position) {
//		this.backgroundWall_position = backgroundWall_position;
//	}
//
//	public String getZuozhe() {
//		return zuozhe;
//	}
//	public void setZuozhe(String zuozhe) {
//		this.zuozhe = zuozhe;
//	}
//	public String getYears() {
//		return years;
//	}
//	public void setYears(String years) {
//		this.years = years;
//	}
//	public String getChaodai() {
//		return chaodai;
//	}
//	public void setChaodai(String chaodai) {
//		this.chaodai = chaodai;
//	}
//	public String getFengge() {
//		return fengge;
//	}
//	public void setFengge(String fengge) {
//		this.fengge = fengge;
//	}
//	public BufferedImage getArtworks_pic() {
//		return artworks_pic;
//	}
//	public void setArtworks_pic(BufferedImage artworks_pic) {
//		this.artworks_pic = artworks_pic;
//	}
//	public Rectangle getArtworks_rectangle() {
//		return artworks_rectangle;
//	}
//	public void setArtworks_rectangle(Rectangle artworks_rectangle) {
//		this.artworks_rectangle = artworks_rectangle;
//	}
//	public Integer getPic_width() {
//		return pic_width;
//	}
//	public void setPic_width(Integer pic_width) {
//		this.pic_width = pic_width;
//	}
//	public Integer getPic_height() {
//		return pic_height;
//	}
//	public void setPic_height(Integer pic_height) {
//		this.pic_height = pic_height;
//	}
//	public Integer getTrue_width() {
//		return true_width;
//	}
//	public void setTrue_width(Integer true_width) {
//		this.true_width = true_width;
//	}
//	public Integer getTrue_height() {
//		return true_height;
//	}
//	public void setTrue_height(Integer true_height) {
//		this.true_height = true_height;
//	}
//	public BufferedImage getBackgroundWall() {
//		return backgroundWall;
//	}
//	public void setBackgroundWall(BufferedImage backgroundWall) {
//		this.backgroundWall = backgroundWall;
//	}
//	public BufferedImage getHuakuang() {
//		return huakuang;
//	}
//	public void setHuakuang(BufferedImage huakuang) {
//		this.huakuang = huakuang;
//	}
//	public BufferedImage getKazhi_1() {
//		return kazhi_1;
//	}
//	public void setKazhi_1(BufferedImage kazhi_1) {
//		this.kazhi_1 = kazhi_1;
//	}
//	public Rectangle getKazhi_1_position() {
//		return kazhi_1_position;
//	}
//	public void setKazhi_1_position(Rectangle kazhi_1_position) {
//		this.kazhi_1_position = kazhi_1_position;
//	}
//	public BufferedImage getKazhi_2() {
//		return kazhi_2;
//	}
//	public void setKazhi_2(BufferedImage kazhi_2) {
//		this.kazhi_2 = kazhi_2;
//	}
//	public String getKazhi_2_id() {
//		return kazhi_2_id;
//	}
//	public void setKazhi_2_id(String kazhi_2_id) {
//		this.kazhi_2_id = kazhi_2_id;
//	}
//	public Rectangle getKazhi_2_position() {
//		return kazhi_2_position;
//	}
//	public void setKazhi_2_position(Rectangle kazhi_2_position) {
//		this.kazhi_2_position = kazhi_2_position;
//	}
//}