package com.matrixeater.hacks;

import java.io.File;
import java.io.IOException;

import com.etheller.warsmash.parsers.mdlx.InterpolationType;
import com.hiveworkshop.wc3.mdl.AnimFlag;
import com.hiveworkshop.wc3.mdl.Bone;
import com.hiveworkshop.wc3.mdl.EditableModel;
import com.hiveworkshop.wc3.mdx.MdxUtils;

public class MakeVersions {

	public static void main(final String[] args) throws IOException {
		makeReplicas(InterpolationType.BEZIER);
		makeReplicas(InterpolationType.HERMITE);
	}

	public static void makeReplicas(final InterpolationType interpType) throws IOException {
		final File sourceDir = new File(
				"C:\\Users\\micro\\OneDrive\\Documents\\Warcraft III\\Models\\ReteraCubes\\Work\\Animation\\Linear");
		for (final File linearModel : sourceDir.listFiles()) {
			final EditableModel modelData = MdxUtils.loadEditable(linearModel);
			String flagName = "None";
			for (final Bone bone : modelData.sortedIdObjects(Bone.class)) {
				for (final AnimFlag flag : bone.getAnimFlags()) {
					flag.setInterpType(interpType);
					if (flag.tans()) {
						for (int i = 0; i < flag.size(); i++) {
							final Object value = flag.getValues().get(i);
							flag.getInTans().add(value);
							flag.getOutTans().add(value);
						}
					}
					flagName = flag.getName();
				}
			}
			final File outputFile = new File(
					"C:\\Users\\micro\\OneDrive\\Documents\\Warcraft III\\Models\\ReteraCubes\\Work\\Animation\\"
							+ interpType.name().charAt(0) + interpType.name().toLowerCase().substring(1)
							+ "\\ReteraCube_" + interpType.name().charAt(0)
							+ interpType.name().toLowerCase().substring(1) + flagName + ".mdx");
			outputFile.getParentFile().mkdirs();
			MdxUtils.saveMdx(modelData, outputFile);
		}
	}

}
