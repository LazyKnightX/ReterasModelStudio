package com.hiveworkshop.rms.ui.util;

import com.hiveworkshop.rms.editor.model.GeosetVertex;
import com.hiveworkshop.rms.editor.wrapper.v2.ModelView;
import com.hiveworkshop.rms.ui.application.edit.mesh.viewport.ViewportModelRenderer;
import com.hiveworkshop.rms.ui.gui.modeledit.VertexFilter;
import com.hiveworkshop.rms.util.Vec3;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSnapshottingListCellRenderer2D<TYPE> extends DefaultListCellRenderer {
	private static final int SIZE = 32;
	private static final int QUARTER_SIZE = SIZE / 4;
	private static final int EIGHTH_SIZE = SIZE / 8;
	private final Map<TYPE, ImageIcon> matrixShellToCachedRenderer = new HashMap<>();
	private final ResettableVertexFilter<TYPE> matrixFilter;
	private final ModelView modelDisplay;
	private final ModelView otherDisplay;

	public AbstractSnapshottingListCellRenderer2D(final ModelView modelDisplay, final ModelView otherDisplay) {
		this.modelDisplay = modelDisplay;
		this.otherDisplay = otherDisplay;
		matrixFilter = createFilter();
	}

	protected abstract ResettableVertexFilter<TYPE> createFilter();

	@Override
	public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean iss, final boolean chf) {
		final Color backgroundColor = getBackground();
		setBackground(null);
		final TYPE matrixShell = valueToType(value);
		ImageIcon myIcon = matrixShellToCachedRenderer.get(matrixShell);
		if (myIcon == null) {
			try {
				final BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
				final Graphics graphics = image.getGraphics();
				graphics.setColor(backgroundColor);
				graphics.fill3DRect(0, 0, SIZE, SIZE, true);
				graphics.setColor(backgroundColor.brighter());
				graphics.fill3DRect(EIGHTH_SIZE, EIGHTH_SIZE, SIZE - QUARTER_SIZE, SIZE - QUARTER_SIZE, true);
				if (otherDisplay != null && contains(otherDisplay, matrixShell)) {
					ViewportModelRenderer.drawFittedTriangles(otherDisplay.getModel(), graphics,
							new Rectangle(SIZE, SIZE), (byte) 1, (byte) 2, matrixFilter.reset(matrixShell),
							getRenderVertex(matrixShell));
				}
				if (modelDisplay != null && contains(modelDisplay, matrixShell)) {
					ViewportModelRenderer.drawFittedTriangles(modelDisplay.getModel(), graphics,
							new Rectangle(SIZE, SIZE), (byte) 1, (byte) 2, matrixFilter.reset(matrixShell),
							getRenderVertex(matrixShell));
				}
				graphics.dispose();
				myIcon = new ImageIcon(image);
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
			matrixShellToCachedRenderer.put(matrixShell, myIcon);
		}
		super.getListCellRendererComponent(list, matrixShell.toString(), index, iss, chf);
		setIcon(myIcon);
		return this;
	}

	protected abstract TYPE valueToType(Object value);

	protected abstract Vec3 getRenderVertex(TYPE value);

	protected abstract boolean contains(ModelView modelDisp, TYPE object);

	protected interface ResettableVertexFilter<TYPE> extends VertexFilter<GeosetVertex> {
		ResettableVertexFilter<TYPE> reset(final TYPE matrix);
	}
}
