package paragon.minecraft.wilytextiles.init;

import java.util.function.Supplier;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import paragon.minecraft.library.ContentProvider;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.tileentities.TEBasket;

/**
 * Holder and initializer class for {@link ContainerType} bearing {@link RegistryObject} instances.
 * 
 * @author Malcolm Riley
 */
public class ModContainers extends ContentProvider<ContainerType<?>> {
	
	/* Constants */
	private static final String PREFIX_CONTAINER = "container_";

	public ModContainers() {
		super(ForgeRegistries.CONTAINERS, Textiles.MOD_ID);
	}
	
	/* RegistryObject Fields */
	
	public final RegistryObject<ContainerType<?>> BASKET = this.create(ModBlocks.Names.BASKET, () -> IForgeContainerType.create(TEBasket.ContainerImpl::createClientContainer));
	
	/* Internal Fields */
	
	/**
	 * Creates a {@link RegistryObject} holding desired {@link ContainerType} given the provided registry name and {@link Supplier}.
	 * <p>
	 * The provided name will be prefixed with {@link #PREFIX_CONTAINER}.
	 * 
	 * @param name - The registry name for the {@link ContainerType}
	 * @param containerSupplier - A factory {@link Supplier} of the {@link ContainerType}
	 * @return A {@link RegistryObject} holding the {@link ContainerType}.
	 */
	protected RegistryObject<ContainerType<?>> create(String name, Supplier<ContainerType<?>> containerSupplier) {
		return super.add(PREFIX_CONTAINER + name, containerSupplier);
	}

}
