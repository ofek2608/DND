package com.ofek2608.gim;

import com.ofek2608.utils.MathUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class Gim implements Iterable<Gim> {
	public static final Gim EMPTY = new Gim(Collections.emptyMap());
	public static final Predicate<String> PATH_VALIDATOR = Pattern.compile("([a-z0-9_]+(.[a-z0-9_]+)*)?").asMatchPredicate();
	private static final Gim[] EMPTY_GIM_ARR = new Gim[0];


	private static final class Context {
		private final Gim[] fromIndex;
		private final Map<String,Gim> fromPath;

		private Context(int count) {
			this.fromIndex = new Gim[count];
			this.fromPath = new HashMap<>();
		}
	}

	private static final class Builder {
		private final String[] paths;
		private final Map<String, String> values;

		private final Context context;
		private int currentIndex;

		private Builder(Map<String, String> values) {
			this.paths = getPaths(values);
			this.values = values;
			this.context = new Context(this.paths.length);
		}


		private static String[] getPaths(Map<String, String> values) {
			Queue<String> queue = new ArrayDeque<>();

			values.keySet().stream()
					.filter(key->values.get(key) != null)
					.peek(Builder::validatePath)
					.forEach(queue::add);

			Set<String> paths = new HashSet<>();
			paths.add("");
			while (!queue.isEmpty()) {
				String path = queue.poll();
				if (paths.add(path)) {
					int dot = path.lastIndexOf('.');
					if (dot >= 0)
						queue.add(path.substring(0, dot));
				}
			}

			return paths.stream().sorted().toArray(String[]::new);
		}

		private static void validatePath(String path) {
			if (!PATH_VALIDATOR.test(path))
				throw new IllegalArgumentException("Illegal path: " + path);
		}


		private @Nullable String getPathOrNull() {
			return currentIndex < paths.length ? paths[currentIndex] : null;
		}
	}




	private final Context context;
	private final int globalIndex;

	public final String path;
	public final String name;
	public final int index;

	private final Gim[] children;

	public final @Nullable Gim prevChild;
	public final @Nullable Gim nextChild;
	public final @Nullable Gim firstChild;
	public final @Nullable Gim finalChild;
	private final int grandChildrenEnd;

	public final @Nullable String valueString;
	public final @Nullable Number valueNumber;



	public Gim(Map<String, String> values) {
		this(new Builder(values), null);
	}

	private Gim(Builder builder, @Nullable Gim prevChild) {
		this.context = builder.context;
		this.globalIndex = builder.currentIndex++;
		this.path = builder.paths[globalIndex];

		int dotIndex = path.lastIndexOf('.');
		this.name = path.substring(dotIndex + 1);

		this.index = prevChild == null ? 0 : prevChild.index + 1;

		this.prevChild = prevChild;

		@Nullable String nextPath;

		nextPath = builder.getPathOrNull();


		if (nextPath == null || path.length() > 0 && !nextPath.startsWith(path + ".")) {
			this.children = EMPTY_GIM_ARR;
			this.firstChild = null;
			this.finalChild = null;
			this.grandChildrenEnd = globalIndex + 1;
		} else {
			this.firstChild = new Gim(builder, null);

			List<Gim> children = new ArrayList<>();
			@Nullable
			Gim child = this.firstChild;
			while (child != null) {
				children.add(child);
				child = child.nextChild;
			}

			this.children = children.toArray(EMPTY_GIM_ARR);
			this.finalChild = this.children[this.children.length - 1];
			this.grandChildrenEnd = this.finalChild.grandChildrenEnd;
		}

		nextPath = builder.getPathOrNull();

		if (nextPath == null || dotIndex >= 0 && !nextPath.startsWith(path.substring(0, dotIndex))) {
			this.nextChild = null;
		} else {
			this.nextChild = new Gim(builder, this);
		}



		this.valueString = builder.values.get(path);
		this.valueNumber = MathUtils.parseString(valueString);



		context.fromIndex[globalIndex] = this;
		context.fromPath.put(path, this);
	}














	private Gim(Context context, String path) {
		this.context = context;
		this.path = path;
		this.name = path.substring(path.lastIndexOf('.') + 1);
		this.valueString = null;
		this.valueNumber = null;
		this.index = 0;
		this.globalIndex = -1;
		this.children = EMPTY_GIM_ARR;

		this.prevChild = null;
		this.nextChild = null;
		this.firstChild = null;
		this.finalChild = null;
		this.grandChildrenEnd = 0;
	}













	@Contract(pure = true)
	public Gim get(String subpath) {
		String fullSubpath = path.length() > 0 ? path + '.' + subpath : subpath;
		Gim result = context.fromPath.get(fullSubpath);
		return result == null ? new Gim(context, fullSubpath) : result;
	}





	@Contract(pure = true)
	public Gim getRoot() {
		return context.fromIndex[0];
	}





	@Contract(pure = true)
	public int getChildrenCount() {
		return children.length;
	}

	public boolean hasChildren() {
		return children.length > 0;
	}

	@Contract(pure = true)
	public Gim getChild(int index) {
		return children[index];
	}

	@Contract(pure = true)
	public Gim[] getChildren() {
		return children.clone();
	}

	@Contract(pure = true)
	public Stream<Gim> childrenStream() {
		return Stream.of(children);
	}

	@Contract(pure = true)
	@NotNull
	@Override
	public Iterator<Gim> iterator() {
		return new Iterator<>() {
			@Nullable Gim next = firstChild;
			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public Gim next() {
				@Nullable
				Gim next = this.next;
				if (next == null) throw new NoSuchElementException();
				this.next = next.nextChild;
				return next;
			}
		};
	}




	@Contract(pure = true)
	public int getGrandChildrenCount() {
		return this.grandChildrenEnd - this.globalIndex;
	}

	@Contract(pure = true)
	public Gim[] getGrandChildren() {
		if (this.globalIndex < 0)
			return new Gim[]{this};
		return Arrays.copyOfRange(context.fromIndex, this.globalIndex, this.grandChildrenEnd);
	}

	@Contract(pure = true)
	public Stream<Gim> getGrandChildrenStream() {
		return Stream.of(getGrandChildren());
	}









	@Contract(pure = true)
	public boolean hasValue() {
		return valueString != null;
	}


	@Contract(pure = true,  value =   "!null->!null")
	public @Nullable String getString(@Nullable String def) {
		return valueString == null ? def : valueString;
	}

	@Contract(pure = true,  value =   "!null->!null")
	public @Nullable Number getNumber(@Nullable Number def) {
		return valueNumber == null ? def : valueNumber;
	}

	@Contract(pure = true) public byte    getByte   (byte    def){ return getNumber(def        ).byteValue  (); }
	@Contract(pure = true) public short   getShort  (short   def){ return getNumber(def        ).shortValue (); }
	@Contract(pure = true) public int     getInt    (int     def){ return getNumber(def        ).intValue   (); }
	@Contract(pure = true) public long    getLong   (long    def){ return getNumber(def        ).longValue  (); }
	@Contract(pure = true) public float   getFloat  (float   def){ return getNumber(def        ).floatValue (); }
	@Contract(pure = true) public double  getDouble (double  def){ return getNumber(def        ).doubleValue(); }
	@Contract(pure = true) public boolean getBoolean(boolean def){ return getNumber(def ? 1 : 0).longValue() != 0; }

	@Contract(pure = true) public byte    getByte   (){ return getByte   ((byte)0); }
	@Contract(pure = true) public short   getShort  (){ return getShort  ((short)0); }
	@Contract(pure = true) public int     getInt    (){ return getInt    (0); }
	@Contract(pure = true) public long    getLong   (){ return getLong   (0L); }
	@Contract(pure = true) public float   getFloat  (){ return getFloat  (0F); }
	@Contract(pure = true) public double  getDouble (){ return getDouble (0D); }
	@Contract(pure = true) public boolean getBoolean(){ return getBoolean(false); }
}
