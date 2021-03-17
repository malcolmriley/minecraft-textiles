package paragon.minecraft.wilytextiles.init;

import java.util.function.Supplier;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import paragon.minecraft.library.ContentProvider;
import paragon.minecraft.wilytextiles.Textiles;
import paragon.minecraft.wilytextiles.tileentities.TEBasket;

public class ModContainers extends ContentProvider<ContainerType<?>> {

	public ModContainers() {
		super(ForgeRegistries.CONTAINERS, Textiles.MOD_ID);
	}
	
	/* RegistryObject Fields */
	
	public final RegistryObject<ContainerType<?>> BASKET = this.create(ModBlocks.Names.BASKET, () -> IForgeContainerType.create(TEBasket.ContainerImpl::createClientContainer));
	
	/* Internal Fields */
	
	protected RegistryObject<ContainerType<?>> create(String name, Supplier<ContainerType<?>> containerSupplier) {
		return super.add("container_" + name, containerSupplier);
	}

}
