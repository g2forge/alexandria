package com.g2forge.alexandria.annotations.note;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

import com.g2forge.alexandria.annotations.ElementAnnotations;
import com.g2forge.alexandria.annotations.HAnnotationProcessor;
import com.g2forge.alexandria.annotations.IAnnotationHandler;
import com.g2forge.alexandria.annotations.note.NoteRecord.IType;

abstract class ANoteAnnotationHandler<T extends Annotation> implements IAnnotationHandler<T> {
	protected static class ClassType implements IType {
		protected final Class<?> ref;

		public ClassType(Class<?> ref) {
			this.ref = ref;
		}

		@Override
		public String getQualifiedName() {
			return ref.getCanonicalName();
		}

		@Override
		public String getSimpleName() {
			return ref.getSimpleName();
		}
	}

	protected IType getRef(ProcessingEnvironment processingEnvironment, Supplier<Class<?>> supplier) {
		try {
			final Class<?> ref = supplier.get();
			return new ClassType(ref);
		} catch (MirroredTypeException e) {
			final TypeMirror mirror = e.getTypeMirror();

			final TypeMirror noRefMirror = processingEnvironment.getElementUtils().getTypeElement(Note.NoRef.class.getCanonicalName()).asType();
			if (processingEnvironment.getTypeUtils().isSameType(mirror, noRefMirror)) return null;
			//processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("%1$s %2$s", mirror, noRefMirror));

			final TypeElement element = HAnnotationProcessor.toTypeElement(processingEnvironment, mirror);
			return new IType() {
				@Override
				public String getQualifiedName() {
					return element.getQualifiedName().toString();
				}

				@Override
				public String getSimpleName() {
					return element.getSimpleName().toString();
				}
			};
		}
	}

	@Override
	public void handle(ProcessingEnvironment processingEnvironment, Collection<ElementAnnotations<T>> elementAnnotations) {
		final Function<? super ElementAnnotations<T>, ? extends ElementAnnotations<NoteRecord>> mapper = ea -> new ElementAnnotations<>(ea.getElement(), ea.getPath(), NoteRecord.class, Stream.of(ea.getAnnotations()).map(a -> toRecord(processingEnvironment, a)).collect(Collectors.toList()).toArray(new NoteRecord[0]));
		internal(processingEnvironment, elementAnnotations.stream().map(mapper).collect(Collectors.toList()));
	}

	protected void internal(ProcessingEnvironment processingEnvironment, Collection<ElementAnnotations<NoteRecord>> elementAnnotations) {
		final Messager messager = processingEnvironment.getMessager();

		for (ElementAnnotations<NoteRecord> elementAnnotation : elementAnnotations) {
			final Supplier<String> path = elementAnnotation.getPath();
			for (NoteRecord annotation : elementAnnotation.getAnnotations()) {
				final String message = annotation.toString(path);
				messager.printMessage(annotation.getType().kind, message, elementAnnotation.getElement());
			}
		}
	}

	protected abstract NoteRecord toRecord(ProcessingEnvironment processingEnvironment, T annotation);
}
