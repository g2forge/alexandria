package com.g2forge.alexandria.java.core.marker;

import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.NoteType;

/**
 * A marker interface for classes which implement the singleton pattern. Please take care when creating singletons, as there are serious challenges in
 * implementing the global logic correctly in the presence of threading. Additionally, there are many design patterns which may suit your needs better such as
 * dependency injection or an enumeration.
 * 
 * If you decide to implement a singleton please make sure that 1) you use a static method to get it, we recommend <code>public static T create();</code> and 2)
 * that it contains no state. Singletons may be useful in cases where you will have multiple implementations of a stateless interface, as in the strategy
 * pattern.
 */
@Note(type = NoteType.TODO, value = "Link to strategy pattern")
public interface ISingleton {}
