package com.g2forge.alexandria.media;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MediaType implements IMediaType {
	Text(true, new SimpleFileExtensions("txt"), new MimeType("text", "plain")),
	Java(true, new SimpleFileExtensions("java")),
	Class(false, new SimpleFileExtensions("class")),
	JAR(false, new SimpleFileExtensions("jar"), new MimeType("application", "java-archive")),
	XML(true, new SimpleFileExtensions("xml"), new MimeType("application", "xml")),
	JSON(true, new SimpleFileExtensions("json"), new MimeType("application", "json")),
	YAML(true, new SimpleFileExtensions("yaml", "yml"), new MimeType("application", "yaml")),
	CSV(true, new SimpleFileExtensions("csv"), new MimeType("text", "csv")),
	ZIP(false, new SimpleFileExtensions("zip"), new MimeType("application", "zip")),
	TAR(false, new SimpleFileExtensions("tar"), new MimeType("application", "x-tar")),
	GZ(false, new SimpleFileExtensions("gz"), new MimeType("application", "gzip")),
	JPG(false, new SimpleFileExtensions("jpg", "jpeg"), new MimeType("image", "jpeg")),
	PNG(false, new SimpleFileExtensions("png"), new MimeType("image", "png")),
	SVG(true, new SimpleFileExtensions("svg"), new MimeType("image", "svg+xml")),
	HTML(true, new SimpleFileExtensions("html", "htm"), new MimeType("text", "html")),
	CSS(true, new SimpleFileExtensions("css"), new MimeType("text", "css")),
	Markdown(true, new SimpleFileExtensions("md"), new MimeType("text", "markdown")),
	MP3(false, new SimpleFileExtensions("mp3"), new MimeType("audio", "mpeg")),
	M4A(false, new SimpleFileExtensions("m4a"), new MimeType("audio", "mp4")),
	M4V(false, new SimpleFileExtensions("m4v", "mp4"), new MimeType("video", "mp4")),
	MKV(false, new SimpleFileExtensions("mkv"), new MimeType("video", "x-matroska")),
	M3U8(true, new SimpleFileExtensions("m3u8"), new MimeType("application", "vnd.apple.mpegurl"));

	@Getter(lazy = true)
	private static final IMediaRegistry registry = new MediaRegistry(MediaType.values());

	protected final boolean text;

	protected final IFileExtensions fileExtensions;

	protected final MimeType mimeType;

	private MediaType(boolean text, IFileExtensions fileExtensions) {
		this(text, fileExtensions, null);
	}
}
