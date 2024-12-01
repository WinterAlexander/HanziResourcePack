package com.winteralexander.hanzirp;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 制作包含汉字的纹理
 * <p>
 * Created on 2024-11-30.
 *
 * @author Alexander Winter
 */
public class 汉字纹理制作者 {
	public static BufferedImage 制作纹理(String hanzi, String pinyin, String fontName, int size, BufferedImage background) {
		BufferedImage output = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);

		Graphics2D graphics = output.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		boolean white = true;

		if(background != null) {
			graphics.drawImage(background, 0, 0, size, size, (_, _, _, _, _, _) -> false);

			float avgR = 0f;
			float avgG = 0f;
			float avgB = 0f;

			for(int i = 0; i < background.getWidth(); i++) {
				for(int j = 0; j < background.getHeight(); j++) {
					int color = background.getRGB(i, j);
					int r = (color >> 8) & 0xFF;
					int g = (color >> 16) & 0xFF;
					int b = (color >> 24) & 0xFF;
					avgR += (float)r / (background.getWidth() * background.getHeight());
					avgG += (float)g / (background.getWidth() * background.getHeight());
					avgB += (float)b / (background.getWidth() * background.getHeight());
				}
			}

			avgR /= 255f;
			avgG /= 255f;
			avgB /= 255f;
			float gray = 0.299f * avgR + 0.587f * avgG + 0.114f * avgB;
			white = gray < 0.5f;
		} else {
			graphics.setColor(Color.BLACK);
			graphics.fillRect(0, 0, size, size);
		}

		Font font;
		FontMetrics metrics;
		int fontSize = size + 1;

		graphics.setColor(white ? Color.WHITE : Color.BLACK);

		do {
			fontSize--;
			font = new Font(fontName, Font.BOLD, fontSize);
			metrics = graphics.getFontMetrics(font);
		} while(metrics.getHeight() * (pinyin == null ? 1 : 2) > size
				|| metrics.stringWidth(hanzi) > size);

		if(pinyin != null) {
			graphics.setFont(font);
			graphics.drawString(hanzi,
					size / 2 - metrics.stringWidth(hanzi) / 2,
					size * 3 / 4 + metrics.getHeight() / 2 - metrics.getDescent());

			Font 拼音Font;
			FontMetrics 拼音Metrics;
			int 拼音Size = fontSize + 1;

			do {
				拼音Size--;
				拼音Font = new Font(fontName, Font.PLAIN, 拼音Size);
				拼音Metrics = graphics.getFontMetrics(拼音Font);
			} while(拼音Metrics.getHeight() * 2 > size
					|| 拼音Metrics.stringWidth(pinyin) > size);

			graphics.setFont(拼音Font);
			graphics.drawString(pinyin,
					size / 2 - 拼音Metrics.stringWidth(pinyin) / 2,
					size / 4 + 拼音Metrics.getHeight() / 2 - 拼音Metrics.getDescent());
		} else {
			graphics.setFont(font);

			graphics.drawString(hanzi,
					size / 2 - metrics.stringWidth(hanzi) / 2,
					size / 2 + metrics.getHeight() / 2 - metrics.getDescent());
		}

		graphics.dispose();
		return output;
	}
}
