package net.slayer5934.chococraft.utils.inject;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassInjector {
	@SuppressWarnings("unchecked")
	public static <T> T createFromField(Field field) {
		try {
			if (field.getType().getConstructors().length == 1 && field.getType().getConstructors()[0].getParameterCount() == 0) return (T) field.getType().newInstance();
			
			Method factoryMethod = Arrays.stream(field.getType().getDeclaredMethods()).filter(m -> m.getAnnotation(InstanceFactoryMethod.class) != null && m.getReturnType() == field.getType()).findFirst().orElse(null);
			
			if (factoryMethod == null) factoryMethod = Arrays.stream(field.getDeclaringClass().getMethods()).filter(m -> m.getAnnotation(InstanceFactoryMethod.class) != null && m.getReturnType() == field.getType()).findFirst().orElse(null);
			
			if (factoryMethod == null) throw new RuntimeException("No suitable 0 parameter constructor or instance factory found for " + field.getName() + " (" + field.getType() + ")");
			
			if (factoryMethod.getParameterCount() > 0) {
				Class<?> paramClass = factoryMethod.getParameterTypes()[0];
				for (Annotation annotation : field.getAnnotations()) {
					if (annotation.annotationType() == paramClass) {
						return (T) factoryMethod.invoke(null, annotation);
					}
				}
			}
			else {
				return (T) factoryMethod.invoke(null);
			}
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		throw new RuntimeException("Unable to create instance for " + field.getType() + ". No empty constructor found and no factory method present");
	}
	
	@Nullable
	public static <T> T getOrNull(Field field) {
		return getOrNull(field, null);
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	public static <T> T getOrNull(Field field, @Nullable Object from) {
		try {
			field.setAccessible(true);
			return (T) field.get(from);
		}
		catch (IllegalAccessException ignored) {
			// PIKACHU GO!!
		}
		return null;
	}
}
