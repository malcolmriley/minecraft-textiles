package paragon.minecraft.wilytextiles.init;

import java.util.function.Supplier;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.internal.ContentProvider;
import paragon.minecraft.wilytextiles.tileentities.TEBasket;

/**
 * Holder and initializer class for {@link MenuType} bearing {@link RegistryObject} instances.
 * 
 * @author Malcolm Riley
 */
public class ModContainers extends ContentProvider<MenuType<?>> {
	
	/* Constants */
	private static final String PREFIX_CONTAINER = "container_";

	public ModContainers() {
		super(ForgeRegistries.CONTAINERS, Textiles.MOD_ID);
	}
	
	/* RegistryObject Fields */
	
	public final RegistryObject<MenuType<?>> BASKET = this.create(ModBlocks.Names.BASKET, () -> IForgeMenuType.create(TEBasket.BasketMenu::create));
	
	/* Internal Fields */
	
	/**
	 * Creates a {@link RegistryObject} holding desired {@link MenuType} given the provided registry name and {@link Supplier}.
	 * <p>
	 * The provided name will be prefixed with {@link #PREFIX_CONTAINER}.
	 * 
	 * @param name - The registry name for the {@link MenuType}
	 * @param containerSupplier - A factory {@link Supplier} of the {@link MenuType}
	 * @return A {@link RegistryObject} holding the {@link MenuType}.
	 */
	protected RegistryObject<MenuType<?>> create(String name, Supplier<MenuType<?>> containerSupplier) {
		return super.add(PREFIX_CONTAINER + name, containerSupplier);
	}

}
