//package com.hd.wlj.duohaowan.util;
//
//import android.graphics.BitmapFactory;
//
//import java.awt.AlphaComposite;
//import java.awt.Color;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.Image;
//import java.awt.Point;
//import java.awt.Rectangle;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.LinkedList;
//import java.util.List;
//
//import javax.imageio.ImageIO;
//
//public class ImageUtil {
//
//	public static void main(String[] args) throws Exception {
////		String srcImageFile = "/home/lymava/简介.jpg";
////		String result = "/home/lymava/简介1.jpg";
//
////		resizeMax(srcImageFile, result, 500, 500);
//
//		File file = new File("/home/lymava/workhome/program/开发项目/林多好玩/测试合成/huakuang.jpg");
////
//		File file_zuopin = new File("/home/lymava/workhome/program/开发项目/林多好玩/测试合成/zuopin.jpg");
////
//		FileOutputStream file_zuopin_out = new FileOutputStream("/home/lymava/workhome/program/开发项目/林多好玩/测试合成/out.jpg");
//
//
//		BitmapFactory.decodeStream()
//
//
//		BufferedImage backGroudImage = ImageIO.read(file);
//
//		List<Rectangle> searchRectangleList = searchRectangleList(backGroudImage);
//
//		Rectangle findRectangle = searchRectangleList.get(0);
//
//		System.out.println(findRectangle);
//
//		BufferedImage bufferedImage_input = ImageIO.read(file_zuopin);
//
//		Double suofang = null;
//
//		BufferedImage imageSynthesis = imageSynthesis(backGroudImage, bufferedImage_input,findRectangle,suofang);
//
//		boolean hasNotAlpha = !backGroudImage.getColorModel().hasAlpha();
//
//        ImageIO.write(imageSynthesis, hasNotAlpha ? "jpg" : "png", file_zuopin_out);
//	}
//
//	public static  BufferedImage imageSynthesis(BufferedImage backGroudImage,BufferedImage bufferedImage_input,Rectangle findRectangle,Double suofang){
//
//		Image scaledInstanceMax = bufferedImage_input;
//
//		Rectangle resizeRectangle  = findRectangle;
//
//		if(suofang != null && suofang > 0 && suofang != 1){
//			resizeRectangle = resizeRectangle(findRectangle, suofang);
//		}
//
////		if(bufferedImage_input.getWidth() != resizeRectangle.getWidth() || bufferedImage_input.getHeight() != resizeRectangle.getHeight()){
////			scaledInstanceMax = getScaledInstanceMax(bufferedImage_input, resizeRectangle);
////		}
//
//		BufferedImage imageSynthesis = imageSynthesis(backGroudImage, scaledInstanceMax,resizeRectangle);
//
//		return imageSynthesis;
//	}
//
//
//    public static boolean write(BufferedImage bufferedImage, OutputStream output) throws IOException {
//
//    	boolean hasNotAlpha = !bufferedImage.getColorModel().hasAlpha();
//
//    	 boolean write = ImageIO.write(bufferedImage, hasNotAlpha ? "jpg" : "png", output);
//
//    	return write;
//    }
//
//    public static Rectangle searchRectangleOne(BufferedImage bufferedImage){
//    	 List<Rectangle> searchRectangleList = searchRectangleList(bufferedImage);
//    	 if(searchRectangleList != null && searchRectangleList.size() > 0){
//    		 return searchRectangleList.get(0);
//    	 }
//    	 return null;
//    }
//
//    public static List<Rectangle> searchRectangleList(BufferedImage bufferedImage){
//
//    	List<Rectangle> list_searchRectangle = new LinkedList<Rectangle>();
//
//		Rectangle searchRectangle = ImageUtil.searchRectangle(bufferedImage,list_searchRectangle);
//
//		while(searchRectangle != null){
//
//				boolean intersects =  false;
//				//做碰撞检测
//				for (Rectangle rectangle_tmp : list_searchRectangle) {
//					intersects = rectangle_tmp.intersects(searchRectangle);
//					if(intersects){
//						int min_x = Math.min((int)searchRectangle.getX(), (int)rectangle_tmp.getX());
//						int min_y = Math.min((int)searchRectangle.getY(), (int)rectangle_tmp.getY());
//
//						int max_width 	= Math.max((int)searchRectangle.getWidth(), (int)rectangle_tmp.getWidth());
//						int max_height 	= Math.max((int)searchRectangle.getHeight(), (int)rectangle_tmp.getHeight());
//
//						rectangle_tmp.setBounds(min_x, min_y, max_width+1, max_height+1);
//						break;
//					}
//				}
//			if(!intersects){
//				list_searchRectangle.add(searchRectangle);
//			}
//
//			searchRectangle = ImageUtil.searchRectangle(bufferedImage,list_searchRectangle);
//		}
//		//搜索出的结果合并
//
//		return list_searchRectangle;
//    }
//
//    /**
//     *
//     * @param bufferedImage
//     * @param list_searchRectangle_notIn 不在这些框内内找
//     * @return
//     */
//    public static Rectangle searchRectangle(BufferedImage bufferedImage,List<Rectangle> list_searchRectangle_notIn){
//		int white_rgb = Color.white.getRGB();
//
//		int width = bufferedImage.getWidth();
//		int height = bufferedImage.getHeight();
//
//		Rectangle findRectangle = null;
//
//		for(int x=0;x<width;x++){
//			for(int y=0;y<height;y++){
//
//				boolean contains = false;
//
//				if(list_searchRectangle_notIn != null){
//
//					Point point = new Point(x, y);
//
//					for (Rectangle rectangle : list_searchRectangle_notIn) {
//						contains = rectangle.contains(point);
//						if(contains){
//							break;
//						}
//					}
//				}
//				if(!contains){
//					 int rgb = bufferedImage.getRGB(x, y);
//			          if(white_rgb == rgb){
//			        	  findRectangle = findRectangle(bufferedImage, x, y);
//			        	  if(findRectangle != null && findRectangle.getWidth() > 50 && findRectangle.getHeight() > 50){
//			        		  break;
//			        	  }
//			          }
//				}
//
//			}
//			if(findRectangle != null && findRectangle.getWidth() > 50 && findRectangle.getHeight() > 50){
//      		  break;
//      	  	}
//		}
//
//		if(findRectangle == null || findRectangle.getWidth() < 50 || findRectangle.getHeight() < 50){
//			findRectangle = null;
//    	 }
//
//		return findRectangle;
//	}
//	public static Rectangle searchRectangle(BufferedImage bufferedImage){
//		Rectangle searchRectangle = searchRectangle(bufferedImage,null);
//		return searchRectangle;
//	}
//
//	public static Rectangle findRectangle(BufferedImage bufferedImage,int x,int y){
//
//		 	int white_rgb = Color.white.getRGB();
//
//		 	int rgb = bufferedImage.getRGB(x, y);
//
//		    if(white_rgb != rgb){
//	        	 return null;
//	        }
//
//			int width = bufferedImage.getWidth();
//			int height = bufferedImage.getHeight();
//
//			int width_max = 0;
//			int height_max = 0;
//
//			boolean width_ok = false;
//
//			boolean height_ok = false;
//
//			while(!width_ok || !height_ok){
//
//				if(x+width_max+1 >= width){
//					width_ok = true;
//				}
//				if(y+height_max+1 >= height){
//					height_ok = true;
//				}
//
//				if(!height_ok){
//					//x轴
//					int height_tmp = y+height_max+1;
//					for(int j=x;j<x+width_max;j++){
//
//						rgb = bufferedImage.getRGB(j, height_tmp);
//
//				          int red = (rgb & 0xff0000 ) >> 16 ;
//				          int green = (rgb & 0xff00 ) >> 8 ;
//				          int blue = (rgb & 0xff );
//
//					    if(red < 220 || green < 220 || blue < 220){
//					    	height_ok = true;
//					    	break;
//				        }
//					}
//				}
//
//				if(!width_ok){
//					//x轴
//					int width_tmp = x+width_max+1;
//					for(int i=y;i<y+height_max;i++){
//
//						rgb = bufferedImage.getRGB(width_tmp, i);
//
//					    int red = (rgb & 0xff0000 ) >> 16 ;
//				          int green = (rgb & 0xff00 ) >> 8 ;
//				          int blue = (rgb & 0xff );
//
//					    if(red < 220 || green < 220 || blue < 220){
//					    	width_ok = true;
//					    	break;
//				        }
//					}
//				}
//				//y轴
//				if(!width_ok){
//					width_max++;
//				}
//				if(!height_ok){
//					height_max++;
//				}
//			}
//
//		return new Rectangle(x, y, width_max, height_max);
//	}
//
//	/**
//	 * 图片合成
//	 * @param backGroudImage	图片背景 原图
//	 * @param bufferedImage_input	合成进入的图
//	 * @param x			合成的图摆放的x坐标
//	 * @param y			合成的图摆放的y坐标
//	 * @param width		合成图的宽
//	 * @param height	合成图的高
//	 * @return	合成后的 BufferedImage	被合成的图像会自动居中
//	 */
//	public static BufferedImage watermark(BufferedImage backGroudImage,BufferedImage bufferedImage_input){
//
//		int backGroudImage_width = backGroudImage.getWidth();
//		int backGroudImage_height = backGroudImage.getHeight();
//
//		int bufferedImage_width = bufferedImage_input.getWidth();
//		int bufferedImage_height = bufferedImage_input.getHeight();
//
//
//		int x = 20;
//		int y = backGroudImage_height - bufferedImage_height-x;
//
//
//        Graphics2D graphics = backGroudImage.createGraphics();
//
//        float alpha = 0.5f; // 透明度
//        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));
//        graphics.drawImage(bufferedImage_input, x, y, null);
//
//        graphics.dispose();
//        return backGroudImage;
//	}
//
//	public static Rectangle resizeRectangle(Rectangle rectangle,Double bili){
//
//		int x = (int) rectangle.getX();
//
//		int y = (int) rectangle.getY();
//
//		int height = (int) rectangle.getHeight();
//
//		int width = (int) rectangle.getWidth();
//
//		x += width*(1-bili)/2;
//		y += height*(1-bili)/2;
//
//		height = (int) (height*bili);
//		width = (int) (width*bili);
//
//		Rectangle rectangle_return = new Rectangle(x, y, width, height);
//
//		return rectangle_return;
//	}
//
//	/**
//	 * 图片合成
//	 * @param backGroudImage	图片背景 原图
//	 * @param bufferedImage_input	合成进入的图
//	 * @param x			合成的图摆放的x坐标
//	 * @param y			合成的图摆放的y坐标
//	 * @param width		合成图的宽
//	 * @param height	合成图的高
//	 * @return	合成后的 BufferedImage	被合成的图像会自动居中
//	 */
//	public static BufferedImage imageSynthesis(BufferedImage backGroudImage,Image bufferedImage_input){
//		Rectangle findRectangle = searchRectangle(backGroudImage);
//		imageSynthesis(backGroudImage, bufferedImage_input,findRectangle);
//
//        return backGroudImage;
//	}
//	/**
//	 * 图片合成
//	 * @param backGroudImage	图片背景 原图
//	 * @param bufferedImage_input	合成进入的图
//	 * @param x			合成的图摆放的x坐标
//	 * @param y			合成的图摆放的y坐标
//	 * @param width		合成图的宽
//	 * @param height	合成图的高
//	 * @return	合成后的 BufferedImage	被合成的图像会自动居中
//	 */
//	public static BufferedImage imageSynthesis(BufferedImage backGroudImage,Image bufferedImage_input,Rectangle findRectangle){
//
//
//		Integer width_zuopin = (int) findRectangle.getWidth();
//		Integer height_zuopin = (int) findRectangle.getHeight();
//
//		Image scaledInstance = getScaledInstanceMax(bufferedImage_input, width_zuopin, height_zuopin);
//
//		int width_zuopin_final = scaledInstance.getWidth(null);
//		int height_zuopin_final = scaledInstance.getHeight(null);
//
//		Integer x_zuopin = (int) findRectangle.getX();
//		Integer y_zuopin = (int) findRectangle.getY();
//
//        Graphics2D graphics = backGroudImage.createGraphics();
//
//        if(width_zuopin > width_zuopin_final){
//        	x_zuopin +=  (width_zuopin-width_zuopin_final)/2;
//        }
//        if(height_zuopin > height_zuopin_final){
//        	y_zuopin +=  (height_zuopin-height_zuopin_final)/2;
//        }
//
////        float alpha = 0.5f; // 透明度
////        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,alpha));
//        graphics.drawImage(scaledInstance, x_zuopin, y_zuopin, null);
//        graphics.dispose();
//
//        return backGroudImage;
//	}
//	/**
//	 * 根据根据最大宽高 保持比例缩放图片
//	 * @param image
//	 * @param max_width
//	 * @param max_height
//	 * @return
//	 */
//	public static Image  getScaledInstanceMax(Image image,Rectangle rectangle){
//
//		double max_width = rectangle.getWidth();
//		double max_height = rectangle.getHeight();
//
//		double width = image.getWidth(null);
//		double height = image.getHeight(null);
//
//		if(width > max_width){
//			height = max_width/width * height;
//			width = max_width;
//		}
//
//		if(height > max_height){
//			width = max_height/height * width;
//			height = max_height;
//		}
//
//		Image scaledInstance = image.getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
//		return scaledInstance;
//	}
//	/**
//	 * 根据根据最大宽高 保持比例缩放图片
//	 * @param image
//	 * @param max_width
//	 * @param max_height
//	 * @return
//	 */
//	public static Image  getScaledInstanceMax(Image image,Integer max_width,Integer max_height){
//
//		double width = image.getWidth(null);
//		double height = image.getHeight(null);
//
//		if(width > max_width){
//			height = max_width/width * height;
//			width = max_width;
//		}
//
//		if(height > max_height){
//			width = max_height/height * width;
//			height = max_height;
//		}
//
//		Image scaledInstance = image.getScaledInstance((int)width, (int)height, Image.SCALE_SMOOTH);
//		return scaledInstance;
//	}
//
//	/**
//     * 重调图片尺寸
//     *
//     * @param input
//     *            a {@link java.io.InputStream} object.
//     * @param output
//     *            a {@link java.io.OutputStream} object.
//     * @param width
//     *            a int.
//     * @param height
//     *            a int.
//     * @param maxWidth
//     *            a int.
//     * @param maxHeight
//     *            a int.
//     */
//    public static void resizeMax(String input_file_path, String output_file_path,int maxWidth, int maxHeight) throws Exception {
//    	FileInputStream fileInputStream = new FileInputStream(input_file_path);
//    	FileOutputStream fileOutputStream = new FileOutputStream(output_file_path);
//    	resize(fileInputStream, fileOutputStream, -1, -1, maxWidth, maxHeight);
//    }
//	/**
//     * 重调图片尺寸
//     *
//     * @param input
//     *            a {@link java.io.InputStream} object.
//     * @param output
//     *            a {@link java.io.OutputStream} object.
//     * @param width
//     *            a int.
//     * @param height
//     *            a int.
//     * @param maxWidth
//     *            a int.
//     * @param maxHeight
//     *            a int.
//     */
//    public static void resize(String input_file_path, String output_file_path,int width, int height) throws Exception {
//    	FileInputStream fileInputStream = new FileInputStream(input_file_path);
//    	FileOutputStream fileOutputStream = new FileOutputStream(output_file_path);
//    	resize(fileInputStream, fileOutputStream, width, height, -1, -1);
//    }
//	/**
//     * 重调图片尺寸
//     *
//     * @param input
//     *            a {@link java.io.InputStream} object.
//     * @param output
//     *            a {@link java.io.OutputStream} object.
//     * @param width
//     *            a int.
//     * @param height
//     *            a int.
//     * @param maxWidth
//     *            a int.
//     * @param maxHeight
//     *            a int.
//     */
//    public static void resize(String input_file_path, String output_file_path,int width, int height, int maxWidth, int maxHeight) throws Exception {
//    	FileInputStream fileInputStream = new FileInputStream(input_file_path);
//    	FileOutputStream fileOutputStream = new FileOutputStream(output_file_path);
//    	resize(fileInputStream, fileOutputStream, width, height, maxWidth, maxHeight);
//    }
//	 /**
//     * 重调图片尺寸
//     *
//     * @param input
//     *            a {@link java.io.InputStream} object.
//     * @param output
//     *            a {@link java.io.OutputStream} object.
//     * @param width
//     *            a int.
//     * @param height
//     *            a int.
//     * @param maxWidth
//     *            a int.
//     * @param maxHeight
//     *            a int.
//     */
//    public static void resize(File input_file, File output_file,int width, int height, int maxWidth, int maxHeight) throws Exception {
//    	FileInputStream fileInputStream = new FileInputStream(input_file);
//    	FileOutputStream fileOutputStream = new FileOutputStream(output_file);
//    	resize(fileInputStream, fileOutputStream, width, height, maxWidth, maxHeight);
//    }
//	 /**
//     * 重调图片尺寸
//     *
//     * @param input
//     *            a {@link java.io.InputStream} object.
//     * @param output
//     *            a {@link java.io.OutputStream} object.
//     * @param width
//     *            a int.
//     * @param height
//     *            a int.
//     * @param maxWidth
//     *            a int.
//     * @param maxHeight
//     *            a int.
//     */
//    public static void resize(InputStream input, OutputStream output,int width, int height, int maxWidth, int maxHeight) throws Exception {
//    	//所有都小于1 那么就原图转移
//        if (width < 1 && height < 1 && maxWidth < 1 && maxHeight < 1) {
//            IOUtil.copy(input, output);
//            return;
//        }
//        try {
//
//            BufferedImage img = ImageIO.read(input);
//            boolean hasNotAlpha = !img.getColorModel().hasAlpha();
//            double w = img.getWidth(null);
//            double h = img.getHeight(null);
//            int toWidth;
//            int toHeight;
//            double rate = w / h;
//
//            if (width > 0 && height > 0) {
//                rate = ((double) width) / ((double) height);
//                toWidth = width;
//                toHeight = height;
//            } else if (width > 0) {
//                toWidth = width;
//                toHeight = (int) (toWidth / rate);
//            } else if (height > 0) {
//                toHeight = height;
//                toWidth = (int) (toHeight * rate);
//            } else {
//                toWidth = ((Number) w).intValue();
//                toHeight = ((Number) h).intValue();
//            }
//
//            if (maxWidth > 0 && toWidth > maxWidth) {
//                toWidth = maxWidth;
//                toHeight = (int) (toWidth / rate);
//            }
//            if (maxHeight > 0 && toHeight > maxHeight) {
//                toHeight = maxHeight;
//                toWidth = (int) (toHeight * rate);
//            }
//
//            BufferedImage tag = new BufferedImage(toWidth, toHeight, hasNotAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
//
//            Graphics graphics = tag.getGraphics();
//            //获取缩略图
//            Image scaledInstance = img.getScaledInstance(toWidth, toHeight, Image.SCALE_SMOOTH);
//            // Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
//            graphics.drawImage(scaledInstance, 0, 0, null);
//            graphics.dispose();
//            ImageIO.write(tag, hasNotAlpha ? "jpg" : "png", output);
//        } catch (Exception e) {
//        	e.printStackTrace();
//            throw e;
//        } finally {
//        	if(output != null){
//        		output.close();
//        	}
//        }
//    }
//	/**
//     * 裁剪图片
//     *
//     * @param x a int.
//     * @param y  a int.
//     * @param w a int.
//     * @param h a int.
//     * @param input  a {@link java.io.InputStream} object.
//     * @param output a {@link java.io.OutputStream} object.
//     * @param isPNG  a boolean.
//     */
//    public static void cut_image(InputStream input, OutputStream output, int x,int y, int w, int h) throws Exception {
//        try {
//            BufferedImage srcImg = ImageIO.read(input);
//
//            boolean hasAlpha = srcImg.getColorModel().hasAlpha();
//
//            int tmpWidth = srcImg.getWidth();
//            int tmpHeight = srcImg.getHeight();
//            int xx = Math.min(tmpWidth - 1, x);
//            int yy = Math.min(tmpHeight - 1, y);
//
//            int ww = w;
//            if (xx + w > tmpWidth) {
//                ww = Math.max(1, tmpWidth - xx);
//            }
//            int hh = h;
//            if (yy + h > tmpHeight) {
//                hh = Math.max(1, tmpHeight - yy);
//            }
//
//            BufferedImage dest = srcImg.getSubimage(xx, yy, ww, hh);
//
//            BufferedImage tag = new BufferedImage(w, h, hasAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
//
//            tag.getGraphics().drawImage(dest, 0, 0, null);
//
//            ImageIO.write(tag, hasAlpha ? "png" : "jpg", output);
//        } catch (Exception e) {
//        	e.printStackTrace();
//            throw new Exception(e);
//        } finally {
//        	if(input != null){
//        		input.close();
//        	}
//        	if(output != null){
//        		output.close();
//        	}
//        }
//    }
//}