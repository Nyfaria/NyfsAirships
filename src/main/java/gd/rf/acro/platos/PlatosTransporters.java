package gd.rf.acro.platos;

import com.mojang.blaze3d.platform.InputConstants;
import gd.rf.acro.platos.blocks.BlockControlWheel;
import gd.rf.acro.platos.blocks.NotFullBlock;
import gd.rf.acro.platos.entity.BlockShipEntity;
import gd.rf.acro.platos.entity.BlockShipEntityRenderer;
import gd.rf.acro.platos.items.BoardingStairsItem;
import gd.rf.acro.platos.items.ClearingScytheItem;
import gd.rf.acro.platos.items.ControlKeyItem;
import gd.rf.acro.platos.items.LiftJackItem;
import gd.rf.acro.platos.items.WrenchItem;
import gd.rf.acro.platos.network.NetworkHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

import static net.minecraft.client.resources.language.I18n.get;

@Mod(value = PlatosTransporters.MODID)
public class PlatosTransporters{
	public static final String MODID = "platos";
	public static final CreativeModeTab TAB = CreativeModeTab.TAB_MISC;
	public static KeyMapping SHIP_UP;
	public static KeyMapping SHIP_DOWN;
	public static KeyMapping SHIP_STOP;

	
	public static final EntityType<BlockShipEntity> BLOCK_SHIP_ENTITY_ENTITY_TYPE = createEntity("block_ship",BlockShipEntity::new,1,1);

	public static final Tag.Named BOAT_MATERIAL = BlockTags.bind("platos:boat_material");
	public static final Tag.Named BOAT_MATERIAL_BLACKLIST = BlockTags.bind("platos:boat_material_blacklist");
	public static final Tag.Named SCYTHEABLE = BlockTags.bind("platos:scytheable");



	private static <T extends Animal> EntityType<T> createEntity(String name, EntityType.EntityFactory<T> factory, float width, float height) {
		ResourceLocation location = new ResourceLocation("platos", name);
		EntityType<T> entity = EntityType.Builder.of(factory, MobCategory.CREATURE).sized(width, height).setTrackingRange(64).setUpdateInterval(1).build(location.toString());
		entity.setRegistryName(location);
		return entity;
	}

	static InputConstants.Key getKey(int key) {
		return InputConstants.Type.KEYSYM.getOrCreate(key);
	}

	public PlatosTransporters() {
		final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		registerBlocks();
		registerItems();
		ConfigUtils.checkConfigs();
		NetworkHandler.registerMessages();
		System.out.println("Hello Fabric world!");
		eventBus.addListener(this::setupClient);
		eventBus.addListener(this::registerRenderers);



	}
	private static KeyMapping registerKeybinding(KeyMapping key) {
		ClientRegistry.registerKeyBinding(key);
		return key;
	}

	public void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {

		event.registerEntityRenderer(PlatosTransporters.BLOCK_SHIP_ENTITY_ENTITY_TYPE, BlockShipEntityRenderer::new);
	}
	public void setupClient(final FMLClientSetupEvent event) {

		SHIP_UP = registerKeybinding(new KeyMapping("key.platos.up", GLFW.GLFW_KEY_Z, "category.platos.main"));
		SHIP_DOWN = registerKeybinding(new KeyMapping("key.platos.down", GLFW.GLFW_KEY_C, "category.platos.main"));
		SHIP_STOP = registerKeybinding(new KeyMapping("key.platos.stop", GLFW.GLFW_KEY_V, "category.platos.main"));

	}


	public static final BlockControlWheel BLOCK_CONTROL_WHEEL = new BlockControlWheel(BlockBehaviour.Properties.of(Material.WOOD));
	public static final NotFullBlock BALLOON_BLOCK = new NotFullBlock(BlockBehaviour.Properties.of(Material.WOOL));
	public static final NotFullBlock FLOAT_BLOCK = new NotFullBlock(BlockBehaviour.Properties.of(Material.WOOL));
	public static final NotFullBlock WHEEL_BLOCK = new NotFullBlock(BlockBehaviour.Properties.of(Material.WOOL));
	private void registerBlocks()
	{

		registerBlock(BLOCK_CONTROL_WHEEL,"ship_controller");
		registerBlock(BALLOON_BLOCK,"balloon_block");
		registerBlock(FLOAT_BLOCK,"float_block");
		registerBlock(WHEEL_BLOCK,"wheel_block");
	}

	public static final ControlKeyItem CONTROL_KEY_ITEM = new ControlKeyItem(new Item.Properties().tab(PlatosTransporters.TAB));
	public static final LiftJackItem LIFT_JACK_ITEM = new LiftJackItem(new Item.Properties().tab(PlatosTransporters.TAB));
	public static final WrenchItem WRENCH_ITEM = new WrenchItem(new Item.Properties().tab(PlatosTransporters.TAB));
	public static final ClearingScytheItem CLEARING_SCYTHE_ITEM = new ClearingScytheItem(new Item.Properties().tab(PlatosTransporters.TAB).durability(100));
	public static final BoardingStairsItem BOARDING_STAIRS_ITEM = new BoardingStairsItem(new Item.Properties().tab(PlatosTransporters.TAB));
	private void registerItems()
	{

		registerItem(CONTROL_KEY_ITEM,"control_key");
		registerItem(LIFT_JACK_ITEM,"lift_jack");
		registerItem(WRENCH_ITEM,"wrench");
		registerItem(CLEARING_SCYTHE_ITEM,"clearing_scythe");
		registerItem(BOARDING_STAIRS_ITEM,"boarding_stairs");

	}

	public static void givePlayerStartBook(Player playerEntity)
	{
		if(!playerEntity.getTags().contains("platos_new") && playerEntity.level.isClientSide)
		{
			playerEntity.addItem(createBook("Acro","Plato's Transporters"
					, get("book.platos.page1")
					, get("book.platos.page2")
					, get("book.platos.page3")
					, get("book.platos.page4")
					, get("book.platos.page5")
					, get("book.platos.page6")
					, get("book.platos.page7")
			));

		}
		playerEntity.addTag("platos_new");
	}
	private static ItemStack createBook(String author, String title,Object ...pages)
	{
		ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
		CompoundTag tags = new CompoundTag();
		tags.putString("author",author);
		tags.putString("title",title);
		ListTag contents = new ListTag();
		for (Object page : pages) {
			contents.add(StringTag.valueOf("{\"text\":\""+page+"\"}"));
		}
		tags.put("pages",contents);
		book.setTag(tags);
		return book;
	}

	public static Item registerItem(Item item, String name)
	{
		item.setRegistryName(name);
		ForgeRegistries.ITEMS.register(item);
		return item;
	}

	public static Block registerBlock(Block block, String name)
	{
		BlockItem itemBlock = new BlockItem(block, new Item.Properties().tab(TAB));
		block.setRegistryName(name);
		itemBlock.setRegistryName(name);
		ForgeRegistries.BLOCKS.register(block);
		ForgeRegistries.ITEMS.register(itemBlock);
		return block;
	}


}
