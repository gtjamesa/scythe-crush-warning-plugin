/*
 * Copyright (c) 2017, honeyhoney <https://github.com/honeyhoney>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.scythecrushwarning.overlays;

import com.scythecrushwarning.ScytheCrushWarningConfig;
import com.scythecrushwarning.ScytheCrushWarningPlugin;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.components.TextComponent;

@Slf4j
public class InventoryOverlay extends WidgetItemOverlay
{
	private final ScytheCrushWarningPlugin plugin;
	private final ScytheCrushWarningConfig config;

	public static final String WARNING_TEXT = "CRUSH";

	@Inject
	private InventoryOverlay(ScytheCrushWarningPlugin plugin, ScytheCrushWarningConfig config)
	{
		this.plugin = plugin;
		this.config = config;

		showOnInventory();
		showOnEquipment();
	}

	@Override
	public void renderItemOverlay(final Graphics2D graphics, final int itemId, final WidgetItem widgetItem)
	{
		if (!config.showInventoryOverlay() || !plugin.scytheIds().contains(itemId) || !plugin.isScytheOnCrush() || plugin.isInAllowedRegion())
		{
			return;
		}

		graphics.setFont(FontManager.getRunescapeSmallFont());
		Rectangle bounds = widgetItem.getCanvasBounds();

		FontMetrics fontMetrics = graphics.getFontMetrics();
		int textWidth = fontMetrics.stringWidth(WARNING_TEXT);
		int x = bounds.x + (bounds.width - textWidth);
		int y = bounds.y + 36;

		TextComponent text = new TextComponent();
		text.setText(WARNING_TEXT);
		text.setPosition(new Point(x, y));
		text.setColor(config.inventoryOverlayColor());

		text.render(graphics);

//		if (!config.showLowDoseOverlay() || !feature.isLowOnDoses())
//		{
//			return;
//		}
//
//		if (itemId == ChuggingBarrelConstants.ITEM_ID)
//		{
//			graphics.setFont(FontManager.getRunescapeSmallFont());
//			Rectangle bounds = itemWidget.getCanvasBounds();
//
//			FontMetrics fontMetrics = graphics.getFontMetrics();
//			int textWidth = fontMetrics.stringWidth(ChuggingBarrelConstants.LOW_DOSE_OVERLAY_TEXT);
//			int x = bounds.x + (bounds.width - textWidth);
//			int y = bounds.y + 36;
//
//			TextComponent text = new TextComponent();
//			text.setText(ChuggingBarrelConstants.LOW_DOSE_OVERLAY_TEXT);
//			text.setPosition(new Point(x, y));
//			text.setColor(Color.red);
//
//			text.render(graphics);
//		}
	}
}
