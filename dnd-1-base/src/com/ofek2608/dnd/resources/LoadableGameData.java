package com.ofek2608.dnd.resources;

import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class LoadableGameData extends GameData {
	private static final Stack<Object> CURRENTLY_LOADING = new Stack<>();

	public LoadableGameData(LoadContext ctx) {
		super(ctx.registry, ctx.data.path);

		Field[] fields = ctx.manager.getFields(getClass());

		for (Field field : fields) {
			Res res = field.getAnnotation(Res.class);
			if (res.mapKind() == Object.class) {
				CURRENTLY_LOADING.push(ctx.manager.load(field.getType(), ctx.get(getFieldName(field))));
				continue;
			}

			Set<String> ignore = Stream.of(fields).map(LoadableGameData::getFieldName).collect(Collectors.toSet());
			ignore.remove(getFieldName(field));

			Map<String, Object> map = new HashMap<>();
			ctx.data.childrenStream()
					.filter(child -> !ignore.contains(child.name))
					.forEach(child -> map.put(child.name, ctx.of(child).load(res.mapKind())));
			CURRENTLY_LOADING.push(map);
		}
	}

	private static String getFieldName(Field field) {
		Res res = field.getAnnotation(Res.class);
		return (res.name().length() == 0 ? field.getName() : res.name()).toLowerCase();
	}

	protected static LoadContext fieldCtx() {
		return (LoadContext) CURRENTLY_LOADING.pop();
	}

	@Nullable
	@Contract("!null->!null")
	@SuppressWarnings("unchecked")
	protected static <T> T field(@Nullable T def) {
		Object obj = CURRENTLY_LOADING.pop();
		return obj == null ? def : (T) obj;
	}

	protected static byte    fieldByte   (byte    def) { return field((Number) def         ).byteValue  (); }
	protected static short   fieldShort  (short   def) { return field((Number) def         ).shortValue (); }
	protected static int     fieldInt    (int     def) { return field((Number) def         ).intValue   (); }
	protected static long    fieldLong   (long    def) { return field((Number) def         ).longValue  (); }
	protected static float   fieldFloat  (float   def) { return field((Number) def         ).floatValue (); }
	protected static double  fieldDouble (double  def) { return field((Number) def         ).doubleValue(); }
	protected static boolean fieldBoolean(boolean def) { return field((Number)(def ? 1 : 0)).longValue  () != 0; }

	@Nullable
	protected static <T> T field() { return field(null); }

	protected static <T> T fieldNonnull() { return Objects.requireNonNull(field()); }

	protected static byte    fieldByte   () { return fieldByte((byte)0); }
	protected static short   fieldShort  () { return fieldShort((short)0); }
	protected static int     fieldInt    () { return fieldInt(0); }
	protected static long    fieldLong   () { return fieldLong(0L); }
	protected static float   fieldFloat  () { return fieldFloat(0F); }
	protected static double  fieldDouble () { return fieldDouble(0D); }
	protected static boolean fieldBoolean() { return fieldBoolean(false); }


	protected static @Nullable String fieldStringPS(String prefix, String suffix) {
		@Nullable
		String str = field();
		return str == null ? null : prefix + str + suffix;
	}

	protected static @Nullable String fieldStringP(String prefix) {
		@Nullable
		String str = field();
		return str == null ? null : prefix + str;
	}

	protected static @Nullable String fieldStringS(String suffix) {
		@Nullable
		String str = field();
		return str == null ? null : str + suffix;
	}

}
