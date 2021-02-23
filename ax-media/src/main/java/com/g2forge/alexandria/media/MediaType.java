package com.g2forge.alexandria.media;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MediaType implements IMediaType {
	Text(true, new SimpleFileExtensions("txt")),
	Java(true, new SimpleFileExtensions("java")),
	Class(false, new SimpleFileExtensions("class")),
	XML(true, new SimpleFileExtensions("xml")),
	JSON(true, new SimpleFileExtensions("json")),
	CSV(true, new SimpleFileExtensions("csv")),
	ZIP(false, new SimpleFileExtensions("zip")),
	TAR(false, new SimpleFileExtensions("tar")),
	GZ(false, new SimpleFileExtensions("gz")),
	JPG(false, new SimpleFileExtensions("jpg", "jpeg")),
	PNG(false, new SimpleFileExtensions("png")),
	SVG(true, new SimpleFileExtensions("svg")),
	HTML(true, new SimpleFileExtensions("html")),
	CSS(true, new SimpleFileExtensions("css")),
	Markdown(true, new SimpleFileExtensions("md"));

	@Getter(lazy = true)
	private static final IMediaRegistry registry = new MediaRegistry(MediaType.values());

	protected final boolean text;

	protected final IFileExtensions fileExtensions;
}
