package com.git.onedayrex.picocr.ui;

import com.git.onedayrex.picocr.http.BaiduUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

/**
 * Created by fuxiang.zhong on 2018/2/24.
 */
public class ScreenShotUI extends JFrame{
    private static final long serialVersionUID = 2054302945309253873L;

    private Dimension d;//Dimension 类封装单个对象中组件的宽度和高度（精确到整数）
    private JLabel imageLabel;//覆盖窗体的图片标签
    private Point point_holder,point_release;//按下鼠标时的坐标与释放鼠标后的坐标，依此计算截屏区域
    private BufferedImage screenshot = null;

    public ScreenShotUI() {
        d = Toolkit.getDefaultToolkit().getScreenSize();//获取整个屏幕大小

        setUndecorated(true);//禁用窗体装饰，不显示标题栏，关闭，最小化等
        setSize(d);//设置窗体全屏
        screenshot = snapShot(0,0,(int)d.getWidth(),(int)d.getHeight());//缓冲图片数据
        imageLabel = new JLabel(new ImageIcon(screenshot));//根据图片缓冲构造图片，设为标签，使窗体即为全屏幕像素

        add(imageLabel);//添加标签
        addMouseListener(new ShotListenerMouse());//鼠标点击监听
        addMouseMotionListener(new ShotListenerMotion());//鼠标拖动监听，绘制选区。。。未完成
        setVisible(true);//设置窗体为可见。默认不可见
    }

    /**
     * 根据参数计算区域
     * @param point_holder
     * @param point_release
     */
    private void snapShot(Point point_holder, Point point_release) {
        //获取屏幕数据的缓冲流
        BufferedImage screen = snapShot(point_holder.x, point_holder.y, point_release.x - point_holder.x, point_release.y - point_holder.y);
        String ocrrequest = BaiduUtils.ocrrequest(screen);
        //把内容复制到剪切板
        setSysClipboardText(ocrrequest);
        this.dispose();

    }

    /**
     * 获取屏幕区域的像素
     * @param i 起始点x坐标
     * @param j 起始点y坐标
     * @param width 到达终点时宽度
     * @param height 到达终点时高度
     * @return
     */
    private BufferedImage snapShot(int i, int j, int width, int height) {
        BufferedImage screenshot = null;
        try {
            screenshot = (new Robot()).createScreenCapture(new
                    Rectangle(i,j,width,height));
        } catch (AWTException e) {
            e.printStackTrace();
        }

        return screenshot;
    }

    private class ShotListenerMouse implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            point_holder = e.getPoint();
            System.out.println(point_holder.x + "\r" + point_holder.y);

        }

        @Override
        public void mouseReleased(MouseEvent e) {

            point_release = e.getPoint();
            snapShot(point_holder, point_release);

        }
    }

    private class ShotListenerMotion implements MouseMotionListener{

        @Override
        public void mouseDragged(MouseEvent e) {
            //鼠标拖动时，记录坐标并重绘窗口
            int endx = e.getX();
            int endy = e.getY();

            //临时图像，用于缓冲屏幕区域放置屏幕闪烁
            Image tempImage2=createImage(ScreenShotUI.this.getWidth(),ScreenShotUI.this.getHeight());
            Graphics g =tempImage2.getGraphics();
            g.drawImage(tempImage2, 0, 0, null);
            int x = Math.min(point_holder.x, endx);
            int y = Math.min(point_holder.y, endy);
            int width = Math.abs(endx - point_holder.x)+1;
            int height = Math.abs(endy - point_holder.y)+1;
            // 加上1防止width或height0
            g.setColor(Color.BLUE);
            g.drawRect(x-1, y-1, width+1, height+1);
            //减1加1都了防止图片矩形框覆盖掉
            BufferedImage saveImage = screenshot.getSubimage(x, y, width, height);
            g.drawImage(saveImage, x, y, null);

            ScreenShotUI.this.getGraphics().drawImage(tempImage2,0,0,ScreenShotUI.this);
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    private void setSysClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }

}
