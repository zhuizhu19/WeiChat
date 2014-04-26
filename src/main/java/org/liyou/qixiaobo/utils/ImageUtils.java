package org.liyou.qixiaobo.utils;

import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public final class ImageUtils {

    private static Font definedFont;

    /**
     * 图片水印
     *
     * @param pressImg  水印图片
     * @param targetImg 目标图片
     * @param x         修正值 默认在中间
     * @param y         修正值 默认在中间
     * @param alpha     透明度
     */
    public static void pressImage (String pressImg, String targetImg, int x, int y, float alpha) {
        try {
            File img = new File(targetImg);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            //水印文件
            Image src_biao = ImageIO.read(new File(pressImg));
            int width_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawImage(src_biao, x, y, width_biao, height_biao, null);
            //水印文件结束
            g.dispose();
            ImageIO.write(image, "jpg", img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片水印
     *
     * @param pressImg  水印图片
     * @param targetImg 目标图片
     * @param x         修正值 默认在中间
     * @param y         修正值 默认在中间
     * @param alpha     透明度
     */
    public static OutputStream pressImageOutStream (String pressImg, String targetImg, int x, int y, float alpha) {
        try {
            File img = new File(targetImg);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            //水印文件
            Image src_biao = ImageIO.read(new File(pressImg));
            int width_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawImage(src_biao, x, y, width_biao, height_biao, null);
            //水印文件结束
            g.dispose();
            OutputStream outputStream = new BufferedOutputStream(new ByteArrayOutputStream());
            ImageIO.write(image, "jpg", outputStream);
            return outputStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 文字水印
     *
     * @param pressText 水印文字
     * @param targetImg 目标图片
     * @param fontName  字体名称
     * @param fontStyle 字体样式
     * @param color     字体颜色
     * @param fontSize  字体大小
     * @param x         修正值
     * @param y         修正值
     * @param alpha     透明度
     */
    public static void pressText (String pressText, String targetImg, String fontName, int fontStyle, Color color, int fontSize, int x, int y, float alpha) {
        try {
            File img = new File(targetImg);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            g.setColor(color);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawString(pressText, x, y);
            g.dispose();
            ImageIO.write(image, "jpg", img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 文字水印
     *
     * @param pressText 水印文字
     * @param targetImg 目标图片
     * @param fontName  字体名称
     * @param fontStyle 字体样式
     * @param color     字体颜色
     * @param fontSize  字体大小
     * @param x         修正值
     * @param y         修正值
     * @param alpha     透明度
     */
    public static ByteArrayOutputStream pressTextOutPutStream (List<String> pressTexts, String targetImg, String fontName, int fontStyle, Color color, int fontSize, List<Point> points, float alpha) {
        try {
            File img = new File(targetImg);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            int size = 0;
            if (pressTexts != null && pressTexts.size() != 0) {
                size = pressTexts.size();
            }
            if (points != null && points.size() != 0) {
                size = size > points.size() ? points.size() : size;
            }
            g.setColor(color);
            g.setFont(getDefinedFont());
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            for (int i = 0; i < size; i++) {
                g.drawString(pressTexts.get(i), points.get(i).x, points.get(i).y);
            }
            g.dispose();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            return outputStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Font getDefinedFont () {
        if (definedFont == null) {
            InputStream is = null;
            BufferedInputStream bis = null;
            try {
                is = new ClassPathResource("/font/FZYTK.TTF").getInputStream();
                bis = new BufferedInputStream(is);
                // createFont返回一个使用指定字体类型和输入数据的新 Font。<br>
                // 新 Font磅值为 1，样式为 PLAIN,注意 此方法不会关闭 InputStream
                definedFont = Font.createFont(Font.TRUETYPE_FONT, bis);
                // 复制此 Font对象并应用新样式，创建一个指定磅值的新 Font对象。
                definedFont = definedFont.deriveFont(Font.BOLD, IWeiChat.FONT_SIZE);
            } catch (FontFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != bis) {
                        bis.close();
                    }
                    if (null != is) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return definedFont;
    }

    /**
     * 缩放
     *
     * @param filePath 图片路径
     * @param height   高度
     * @param width    宽度
     * @param bb       比例不对时是否需要补白
     */
    public static void resize (String filePath, int height, int width, boolean bb) {
        try {
            double ratio = 0.0; //缩放比例
            File f = new File(filePath);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);
            //计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bb) {
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null))
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
                else
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);
                g.dispose();
                itemp = image;
            }
            ImageIO.write((BufferedImage) itemp, "jpg", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static int getLength (String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (new String(text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }
}
