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

		//graphics.drawImage(background, 0, 0, size, size, (_, _, _, _, _, _) -> false);

		Font font;
		FontMetrics metrics;
		int fontSize = size + 1;

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
					size / 4 + metrics.getHeight() / 2 - metrics.getDescent());

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
					size * 3 / 4 + 拼音Metrics.getHeight() / 2 - 拼音Metrics.getDescent());
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
