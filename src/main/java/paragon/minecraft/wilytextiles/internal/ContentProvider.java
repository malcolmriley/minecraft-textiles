package paragon.minecraft.wilytextiles.internal;

import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

/**
 * Convenience class for registering content to Forge.
 *
 * @author Malcolm Riley
 * @param <T> The {@link IForgeRegistryEntry} subtype
 */
public abstract class ContentProvider<T extends IForgeRegistryEntry<T>> implements IEventBusListener {

	/* Internal Fields */
	protected final DeferredRegister<T> ALL;

	public ContentProvider(IForgeRegistry<T> registry, String modID) {
		this.ALL = DeferredRegister.create(registry, modID);
	}

	/**
	 * Registers internal {@link DeferredRegister} with the passed {@link IEventBus}, so that the content
	 * of this {@link ContentProvider} will be registered when the time comes.
	 *
	 * @param bus - The bus to register the {@link DeferredRegister} to.
	 */
	public void registerTo(IEventBus bus) {
		this.ALL.register(bus);
	}
	
	/**
	 * Returns an {@link Iterable} over all non-null content instances held by this {@link ContentProvider}.
	 * <p>
	 * {@link ContentProvider} may hold {@link RegistryObject} that return {@code FALSE} for {@link RegistryObject#isPresent()}; such
	 * instances are already filtered out by this method.
	 * 
	 * @return An {@link Iterable} over all non-null content instances.
	 */
	public Iterable<T> iterateContent() {
		return () -> this.streamContent().iterator();
	}

	/**
	 * Returns an {@link Stream} over all non-null content instances held by this {@link ContentProvider}.
	 * <p>
	 * {@link ContentProvider} may hold {@link RegistryObject} that return {@code FALSE} for {@link RegistryObject#isPresent()}; such
	 * instances are already filtered out by this method.
	 * 
	 * @return An {@link Stream} over all non-null content instances.
	 */
	public Stream<T> streamContent() {
		return this.filterUnregistered(this.ALL.getEntries().stream());
	}

	/* Internal Methods */

	protected RegistryObject<T> add(String name, Supplier<T> supplier) {
		return this.ALL.register(name, supplier);
	}

	protected Stream<T> filterUnregistered(Stream<RegistryObject<T>> input) {
		return input.filter(RegistryObject::isPresent).map(RegistryObject::get);
	}

}
