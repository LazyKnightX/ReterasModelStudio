package com.hiveworkshop.rms.editor.model;

import com.hiveworkshop.rms.parsers.mdlx.MdlxTextureAnimation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TextureAnims, inside them called TVertexAnims
 *
 * Eric Theller 3/9/2012
 */
public class TextureAnim extends TimelineContainer {
	/**
	 * Constructor for objects of class TextureAnim
	 */
	public TextureAnim(final AnimFlag flag) {
		add(flag);
	}

	public TextureAnim(final List<AnimFlag> flags) {
		setAnimFlags(flags);
	}

	public TextureAnim(final TextureAnim other) {
		Collection<AnimFlag> flags = new ArrayList<>();
		for (AnimFlag animFlag : other.getAnimFlags()) {
			flags.add(new AnimFlag(animFlag));
		}
		setAnimFlags(flags);
	}

	public TextureAnim(final MdlxTextureAnimation animation) {
		loadTimelines(animation);
	}

	public MdlxTextureAnimation toMdlx() {
		final MdlxTextureAnimation animation = new MdlxTextureAnimation();

		timelinesToMdlx(animation);

		return animation;
	}
}
