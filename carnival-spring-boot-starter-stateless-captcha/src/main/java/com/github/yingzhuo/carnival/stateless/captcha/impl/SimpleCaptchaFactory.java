/*
 *  ____    _    ____  _   _ _____     ___    _
 * / ___|  / \  |  _ \| \ | |_ _\ \   / / \  | |
 * | |    / _ \ | |_) |  \| || | \ \ / / _ \ | |
 * | |___/ ___ \|  _ <| |\  || |  \ V / ___ \| |___
 * \____/_/   \_\_| \_\_| \_|___|  \_/_/   \_\_____|
 *
 * https://github.com/yingzhuo/carnival
 */
package com.github.yingzhuo.carnival.stateless.captcha.impl;

import com.github.yingzhuo.carnival.stateless.captcha.CaptchaDao;
import com.github.yingzhuo.carnival.stateless.captcha.CaptchaFactory;
import com.github.yingzhuo.carnival.stateless.captcha.CaptchaId;
import com.github.yingzhuo.carnival.stateless.captcha.HashedImage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

/**
 * @author 应卓
 */
@Slf4j
public class SimpleCaptchaFactory implements CaptchaFactory {

    private static final Random RANDOM = new Random();

    private int width = 100;
    private int height = 18;
    private final CaptchaDao captchaDao;

    public SimpleCaptchaFactory(CaptchaDao captchaDao) {
        this.captchaDao = captchaDao;
    }

    @Override
    public Pair<CaptchaId, HashedImage> create(int length) {
        try {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            g.setColor(getRandColor(200, 250));
            g.fillRect(1, 1, width - 1, height - 1);
            g.setColor(new Color(102, 102, 102));
            g.drawRect(0, 0, width - 1, height - 1);
            g.setFont(new Font("Times New Roman", Font.PLAIN, 17));
            g.setColor(getRandColor(160, 200));

            // 画随机线
            for (int i = 0; i < 155; i++) {
                int x = RANDOM.nextInt(width - 1);
                int y = RANDOM.nextInt(height - 1);
                int xl = RANDOM.nextInt(6) + 1;
                int yl = RANDOM.nextInt(12) + 1;
                g.drawLine(x, y, x + xl, y + yl);
            }

            // 从另一方向画随机线
            for (int i = 0; i < 70; i++) {
                int x = RANDOM.nextInt(width - 1);
                int y = RANDOM.nextInt(height - 1);
                int xl = RANDOM.nextInt(12) + 1;
                int yl = RANDOM.nextInt(6) + 1;
                g.drawLine(x, y, x - xl, y - yl);
            }

            // 生成随机数,并将随机数字转换为字母
            String captchaValue = "";
            for (int i = 0; i < length; i++) {
                char ctmp = (char) (RANDOM.nextInt(26) + 65);
                captchaValue += String.valueOf(ctmp);
                g.setColor(new Color(20 + RANDOM.nextInt(110), 20 + RANDOM.nextInt(110), 20 + RANDOM.nextInt(110)));
                g.drawString(String.valueOf(ctmp), 15 * i + 10, 16);
            }
            g.dispose();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out);
            byte[] data = out.toByteArray();

            String imageStr = Base64.getEncoder().encodeToString(data);
            String id = UUID.randomUUID().toString();

            final CaptchaId captchaId = CaptchaId.of(id);
            captchaDao.save(captchaId, captchaValue);

            log.info("CAPTCHA: '{}'", captchaValue);

            return Pair.of(captchaId, HashedImage.of(imageStr));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Color getRandColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + RANDOM.nextInt(bc - fc);
        int g = fc + RANDOM.nextInt(bc - fc);
        int b = fc + RANDOM.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}