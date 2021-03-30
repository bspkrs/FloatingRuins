package bspkrs.floatingruins;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import bspkrs.util.BlockNotifyType;
import bspkrs.util.CommonUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
//import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.registries.GameData;

public class WorldGenFloatingIslandRuin extends WorldGenerator
{
    private final int                                               numberOfItems;
    private final String                                            stringOfIds;
    private final String                                            spawnerDefault;
    private final String                                            spawnerDesert;
    private final String                                            spawnerForest;
    private final String                                            spawnerHills;
    private final String                                            spawnerPlains;
    private final String                                            spawnerSwampland;
    private final String                                            spawnerTaiga;
    private final String                                            spawnerOcean;
    private final String                                            spawnerRiver;
    private final String                                            spawnerJungle;
    private final String                                            spawnerIceBiomes;
    private final String                                            spawnerMushroom;
    private final String                                            spawnerNearLava;
    private final boolean                                           isLavaNearby;

    private static final String[]                                   allowedCtgys;

    private static final ArrayList<SimpleEntry<ItemStack, Integer>> helmWeights     = new ArrayList<SimpleEntry<ItemStack, Integer>>();
    private static final ArrayList<SimpleEntry<ItemStack, Integer>> plateWeights    = new ArrayList<SimpleEntry<ItemStack, Integer>>();
    private static final ArrayList<SimpleEntry<ItemStack, Integer>> leggingWeights  = new ArrayList<SimpleEntry<ItemStack, Integer>>();
    private static final ArrayList<SimpleEntry<ItemStack, Integer>> bootWeights     = new ArrayList<SimpleEntry<ItemStack, Integer>>();
    private static final ArrayList<SimpleEntry<ItemStack, Integer>> skelWeapWeights = new ArrayList<SimpleEntry<ItemStack, Integer>>();
    private static final ArrayList<SimpleEntry<ItemStack, Integer>> zombWeapWeights = new ArrayList<SimpleEntry<ItemStack, Integer>>();

    public static ItemStack getWeightedItemStack(Random random, ArrayList<SimpleEntry<ItemStack, Integer>> list)
    {
        int currentWeight = 0;

        for (SimpleEntry<ItemStack, Integer> e : list)
            currentWeight += e.getValue();

        int selection = random.nextInt(currentWeight);

        for (SimpleEntry<ItemStack, Integer> e : list)
        {
            currentWeight -= e.getValue();
            if (selection >= currentWeight)
                return e.getKey();
        }

        return list.get(0).getKey();
    }

    public WorldGenFloatingIslandRuin(boolean isLavaNearby)
    {
        numberOfItems = FloatingRuins.numberOfItems;
        stringOfIds = FloatingRuins.stringOfIds;
        spawnerDefault = FloatingRuins.spawnerDefault;
        spawnerDesert = FloatingRuins.spawnerDesert;
        spawnerForest = FloatingRuins.spawnerForest;
        spawnerHills = FloatingRuins.spawnerHills;
        spawnerPlains = FloatingRuins.spawnerPlains;
        spawnerSwampland = FloatingRuins.spawnerSwampland;
        spawnerTaiga = FloatingRuins.spawnerTaiga;
        spawnerOcean = FloatingRuins.spawnerOcean;
        spawnerRiver = FloatingRuins.spawnerOcean;
        spawnerJungle = FloatingRuins.spawnerJungle;
        spawnerIceBiomes = FloatingRuins.spawnerIceBiomes;
        spawnerMushroom = FloatingRuins.spawnerMushroom;
        spawnerNearLava = FloatingRuins.spawnerNearLava;

        this.isLavaNearby = isLavaNearby;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos pos)
    {
        setDungeon(world, random, pos);
        return true;
    }

    private void setDungeon(World world, Random random, BlockPos pos)
    {
        BiomeGenBase biomegenbase = world.getWorldChunkManager().getBiomeGenerator(pos);
        IBlockState dungeonBlock = getDungeonBlock(biomegenbase);
        if (dungeonBlock.equals(Blocks.SNOW.getDefaultState()))
            setIgloo(world, pos, 5, dungeonBlock);
        else if (dungeonBlock.equals(Blocks.SANDSTONE.getDefaultState()))
            setPyramid(world, pos, 6, dungeonBlock);
        else if (dungeonBlock.equals(Blocks.RED_MUSHROOM_BLOCK.getDefaultState()))
            CommonUtils.setHugeMushroom(world, random, pos, dungeonBlock);
        else
            setBox(world, random, pos, 4, 4, getDungeonBlock(biomegenbase));

        setChest(world, random, pos);
        setSpawner(world, biomegenbase, pos.up(2));
    }

    private void setChest(World world, Random random, BlockPos pos)
    {
        world.setBlockState(pos, Blocks.CHEST.getDefaultState(), BlockNotifyType.ALL);
        TileEntityChest tileentitychest = (TileEntityChest) world.getTileEntity(pos);
        addItems(tileentitychest, random);

        IBlockState blockingBlock = (FloatingRuins.harderDungeons ? Blocks.BEDROCK.getDefaultState() : Blocks.OBSIDIAN.getDefaultState());

        world.setBlockState(pos.north(), blockingBlock, BlockNotifyType.ALL);
        world.setBlockState(pos.south(), blockingBlock, BlockNotifyType.ALL);
        world.setBlockState(pos.east(), blockingBlock, BlockNotifyType.ALL);
        world.setBlockState(pos.west(), blockingBlock, BlockNotifyType.ALL);
        world.setBlockState(pos.down(), blockingBlock, BlockNotifyType.ALL);

        if (FloatingRuins.harderDungeons)
            world.setBlockState(pos.up(), Blocks.OBSIDIAN.getDefaultState(), BlockNotifyType.ALL);
        else
            world.setBlockState(pos.up(), Blocks.COBBLESTONE.getDefaultState(), BlockNotifyType.ALL);
    }

    private void setSpawner(World world, BiomeGenBase biomegenbase, BlockPos pos)
    {
        world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), BlockNotifyType.ALL);
        IBlockState block = Blocks.OBSIDIAN.getDefaultState();

        world.setBlockState(pos.north(), block, BlockNotifyType.ALL);
        world.setBlockState(pos.south(), block, BlockNotifyType.ALL);
        world.setBlockState(pos.east(), block, BlockNotifyType.ALL);
        world.setBlockState(pos.west(), block, BlockNotifyType.ALL);
        world.setBlockState(pos.down(), block, BlockNotifyType.ALL);
        if (FloatingRuins.harderDungeons)
        {
            world.setBlockState(pos.down(), Blocks.OBSIDIAN.getDefaultState(), BlockNotifyType.ALL);
            world.setBlockState(pos.up(), Blocks.OBSIDIAN.getDefaultState(), BlockNotifyType.ALL);
        }

        TileEntityMobSpawner tileEntityMobSpawner = (TileEntityMobSpawner) world.getTileEntity(pos);
        if (tileEntityMobSpawner != null)
        {
            String[] mobIDList;
            if (FloatingRuins.allowMultiMobSpawners)
                mobIDList = getSpawnerMobList(world, biomegenbase);
            else
                mobIDList = new String[] { getSpawnerType(world, biomegenbase, pos) };

            if ((mobIDList.length == 1) && mobIDList[0].trim().equalsIgnoreCase("Default"))
                mobIDList = FloatingRuins.spawnerDefault.split(",");

            // get rid of extra whitespace from list to avoid NPEs
            for (int i = 0; i < mobIDList.length; i++)
                mobIDList[i] = mobIDList[i].trim();

            NBTTagCompound spawnerNBT = new NBTTagCompound();
            tileEntityMobSpawner.writeToNBT(spawnerNBT);

            NBTTagCompound properties;
            NBTTagList spawnPotentials = new NBTTagList();

            for (int i = 0; i < mobIDList.length; i++)
            {
                properties = new NBTTagCompound();

                NBTTagCompound potentialSpawn = new NBTTagCompound();
                potentialSpawn.setInteger("Weight", world.rand.nextInt(4) + 1);
                String debug = "  +" + mobIDList[i] + " wt(" + potentialSpawn.getInteger("Weight") + ") ";

                if (mobIDList[i].equals("WitherSkeleton"))
                {
                    NBTTagList equipment = new NBTTagList();
                    debug += "+E:" + this.applyEquipment(equipment, zombWeapWeights, world.rand, true, world.rand.nextBoolean());
                    properties.setTag("Equipment", equipment);

                    properties.setByte("SkeletonType", (byte) 1);
                    potentialSpawn.setTag("Properties", properties);
                    potentialSpawn.setString("Type", "Skeleton");
                    spawnerNBT.setString("EntityId", "Skeleton");
                    spawnerNBT.setTag("SpawnData", properties);
                }
                else if (mobIDList[i].equals("Wolf"))
                {
                    properties.setByte("Angry", (byte) 1);
                    potentialSpawn.setTag("Properties", properties);
                    potentialSpawn.setString("Type", mobIDList[i]);
                    spawnerNBT.setString("EntityId", mobIDList[i]);
                    spawnerNBT.setTag("SpawnData", properties);
                }
                else if (mobIDList[i].equals("ChargedCreeper"))
                {
                    properties.setByte("powered", (byte) 1);
                    potentialSpawn.setTag("Properties", properties);
                    potentialSpawn.setString("Type", "Creeper");
                    spawnerNBT.setString("EntityId", "Creeper");
                    spawnerNBT.setTag("SpawnData", properties);
                }
                else if (mobIDList[i].equals("PigZombie"))
                {
                    properties.setShort("Anger", (short) (400 + world.rand.nextInt(400)));

                    NBTTagList equipment = new NBTTagList();
                    debug += "+E:" + applyEquipment(equipment, zombWeapWeights, world.rand, true, false);
                    properties.setTag("Equipment", equipment);

                    potentialSpawn.setTag("Properties", properties);
                    potentialSpawn.setString("Type", mobIDList[i]);
                    spawnerNBT.setString("EntityId", mobIDList[i]);
                    spawnerNBT.setTag("SpawnData", properties);
                }
                else if (mobIDList[i].equals("Zombie"))
                {
                    boolean flag = world.rand.nextBoolean();
                    properties.setByte("isVillager", (byte) (flag ? 1 : 0));
                    if (flag)
                    {
                        properties.setInteger("ConversionTime", -1);
                        debug += "+V ";
                    }

                    flag = world.rand.nextBoolean();
                    if (flag)
                    {
                        NBTTagList equipment = new NBTTagList();
                        debug += "+E:" + applyEquipment(equipment, zombWeapWeights, world.rand, flag, world.rand.nextBoolean());
                        properties.setTag("Equipment", equipment);
                    }

                    potentialSpawn.setTag("Properties", properties);
                    potentialSpawn.setString("Type", mobIDList[i]);
                    spawnerNBT.setString("EntityId", mobIDList[i]);
                    spawnerNBT.setTag("SpawnData", properties);
                }
                else if (mobIDList[i].equals("Skeleton"))
                {
                    NBTTagList equipment = new NBTTagList();
                    debug += "+E:" + applyEquipment(equipment, skelWeapWeights, world.rand, true, world.rand.nextBoolean());
                    properties.setTag("Equipment", equipment);

                    potentialSpawn.setTag("Properties", properties);
                    potentialSpawn.setString("Type", mobIDList[i]);
                    spawnerNBT.setString("EntityId", mobIDList[i]);
                    spawnerNBT.setTag("SpawnData", properties);
                }
                else
                {
                    potentialSpawn.setString("Type", mobIDList[i]);
                    spawnerNBT.setString("EntityId", mobIDList[i]);
                    spawnerNBT.setTag("SpawnData", properties);
                }

                spawnPotentials.appendTag(potentialSpawn);
                FloatingRuins.debug(debug);
            }

            spawnerNBT.setTag("SpawnPotentials", spawnPotentials);

            if (FloatingRuins.harderDungeons)
            {
                spawnerNBT.setShort("MinSpawnDelay", (short) 80);
                spawnerNBT.setShort("MaxSpawnDelay", (short) 200);
                spawnerNBT.setShort("SpawnCount", (short) 6);
                spawnerNBT.setShort("MaxNearbyEntities", (short) 16);
                spawnerNBT.setShort("SpawnRange", (short) 7);
            }

            tileEntityMobSpawner.readFromNBT(spawnerNBT);
        }
    }

    private String applyEquipment(NBTTagList equipment, ArrayList<SimpleEntry<ItemStack, Integer>> weaponList, Random random, boolean giveWeapon, boolean giveArmor)
    {
        String debug = "";
        NBTTagCompound Items = new NBTTagCompound();

        ItemStack equip = getWeightedItemStack(random, weaponList);
        if (giveWeapon)
        {
            if (equip != null)
            {
                equip.writeToNBT(Items);
                debug += equip.getDisplayName() + ";";
            }
        }

        equipment.appendTag(Items);

        if (giveArmor)
        {
            Items = new NBTTagCompound();
            equip = getWeightedItemStack(random, bootWeights);
            if (equip != null)
            {
                equip.writeToNBT(Items);
                debug += equip.getDisplayName() + ";";
            }
            equipment.appendTag(Items);

            Items = new NBTTagCompound();
            equip = getWeightedItemStack(random, leggingWeights);
            if (equip != null)
            {
                equip.writeToNBT(Items);
                debug += equip.getDisplayName() + ";";
            }
            equipment.appendTag(Items);

            Items = new NBTTagCompound();
            equip = getWeightedItemStack(random, plateWeights);
            if (equip != null)
            {
                equip.writeToNBT(Items);
                debug += equip.getDisplayName() + ";";
            }
            equipment.appendTag(Items);

            Items = new NBTTagCompound();
            equip = getWeightedItemStack(random, helmWeights);
            if (equip != null)
            {
                equip.writeToNBT(Items);
                debug += equip.getDisplayName() + ";";
            }
            equipment.appendTag(Items);
        }
        else
        {
            for (int j = 0; j < 4; j++)
            {
                Items = new NBTTagCompound();
                equipment.appendTag(Items);
            }
        }

        return debug;
    }

    private IBlockState getDungeonBlock(BiomeGenBase biomegenbase)
    {
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.PLAINS))
            return Blocks.PLANKS.getDefaultState();

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SANDY))
            return Blocks.SANDSTONE.getDefaultState();

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SNOWY))
            return Blocks.SNOW.getDefaultState();

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.FOREST))
            return Blocks.COBBLESTONE.getDefaultState();

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.JUNGLE))
            return Blocks.MOSSY_COBBLESTONE.getDefaultState();

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SWAMP))
            return Blocks.MOSSY_COBBLESTONE.getDefaultState();

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.MOUNTAIN) || BiomeDictionary.isBiomeOfType(biomegenbase, Type.HILLS))
            return Blocks.STONE.getDefaultState();

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.WATER))
            if (biomegenbase.getClass().getSimpleName().toLowerCase(Locale.US).contains("ocean"))
                return Blocks.STONEBRICK.getDefaultState();
            else
                return Blocks.PLANKS.getDefaultState();

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.MUSHROOM))
            return Blocks.RED_MUSHROOM_BLOCK.getDefaultState();
        else
            return Blocks.BRICK_BLOCK.getDefaultState();
    }

    private void addItems(TileEntityChest tileentitychest, Random random)
    {
        int limit = random.nextInt(4) + numberOfItems;
        int i = 0;
        do
        {
            if (FloatingRuins.useCustomItemList)
                tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()), getItems(random));
            else
                tileentitychest.setInventorySlotContents(random.nextInt(tileentitychest.getSizeInventory()),
                        ChestGenHooks.getOneItem(allowedCtgys[random.nextInt(allowedCtgys.length)], random));
        }
        while (++i <= limit);

    }

    private ItemStack getItems(Random random)
    {
        String itemStack[] = stringOfIds.split(";")[random.nextInt(stringOfIds.split(";").length)].split(",");
        String id = Items.EGG.toString();
        int size = 1;
        int meta = 0;
        if (itemStack.length > 0)
            id = itemStack[0].trim();

        if (itemStack.length > 1)
            size = CommonUtils.parseInt(itemStack[1].trim());

        if (itemStack.length > 2)
            meta = CommonUtils.parseInt(itemStack[2].trim());

        Item item = Item.getByNameOrId(id);

        if (item == null)
            item = Items.EGG;

        if (!item.getHasSubtypes() && (meta != 0))
            meta = 0;

        return new ItemStack(item, size, meta);
    }

    public String getSpawnerType(World world, BiomeGenBase biomegenbase, BlockPos pos)
    {
        if (isLavaNearby)
            return getMobString(spawnerNearLava, world, pos);

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.PLAINS))
            return getMobString(spawnerPlains, world, pos);

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SANDY))
            return getMobString(spawnerDesert, world, pos);

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SNOWY) && BiomeDictionary.isBiomeOfType(biomegenbase, Type.FOREST))
            return getMobString(spawnerTaiga, world, pos);

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SNOWY))
            return getMobString(spawnerIceBiomes, world, pos);

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.FOREST))
            return getMobString(spawnerForest, world, pos);

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.JUNGLE))
            return getMobString(spawnerJungle, world, pos);

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SWAMP))
            return getMobString(spawnerSwampland, world, pos);

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.MOUNTAIN) || BiomeDictionary.isBiomeOfType(biomegenbase, Type.HILLS))
            return getMobString(spawnerHills, world, pos);

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.WATER))
            if (biomegenbase.getClass().getSimpleName().toLowerCase(Locale.US).contains("ocean"))
                return getMobString(spawnerOcean, world, pos);
            else
                return getMobString(spawnerRiver, world, pos);

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.MUSHROOM))
            return getMobString(spawnerMushroom, world, pos);
        else
            return getMobString(spawnerDefault, world, pos);
    }

    public String[] getSpawnerMobList(World world, BiomeGenBase biomegenbase)
    {
        if (isLavaNearby)
            return spawnerNearLava.split(",");

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.PLAINS))
            return spawnerPlains.split(",");

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SANDY))
            return spawnerDesert.split(",");

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SNOWY) && BiomeDictionary.isBiomeOfType(biomegenbase, Type.FOREST))
            return spawnerTaiga.split(",");

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SNOWY))
            return spawnerIceBiomes.split(",");

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.FOREST))
            return spawnerForest.split(",");

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.JUNGLE))
            return spawnerJungle.split(",");

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SWAMP))
            return spawnerSwampland.split(",");

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.MOUNTAIN) || BiomeDictionary.isBiomeOfType(biomegenbase, Type.HILLS))
            return spawnerHills.split(",");

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.WATER))
            if (biomegenbase.getClass().getSimpleName().toLowerCase(Locale.US).contains("ocean"))
                return spawnerOcean.split(",");
            else
                return spawnerRiver.split(",");

        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.MUSHROOM))
            return spawnerMushroom.split(",");
        else
            return spawnerDefault.split(",");
    }

    private String getMobString(String s, World world, BlockPos pos)
    {
        Random random = new Random();
        if (s.equalsIgnoreCase("default"))
            return getMobString(spawnerDefault, world, pos);
        else
        {
            String mob = s.split(",")[random.nextInt(s.split(",").length)].trim();
            if (mob.equalsIgnoreCase("slime"))
            {
                EntitySlime slime = new EntitySlime(world);
                slime.setPosition(pos.getX(), pos.getY(), pos.getZ());
                if (!(new EntitySlime(world)).getCanSpawnHere())
                    return getMobString(spawnerDefault, world, pos);
            }

            return mob;
        }
    }

    public void setBox(World world, Random random, BlockPos pos, int width, int height, IBlockState block)
    {
        for (int x = -width; x <= width; x++)
            for (int y = height; y >= -height; y--)
                for (int z = -width; z <= width; z++)
                {
                    BlockPos delta = pos.add(new BlockPos(x, y, z));
                    if ((y == height) || ((Math.abs(x) == width) && (Math.abs(z) == width) && (y >= 0)))
                    {
                        world.setBlockState(delta, Blocks.STONEBRICK.getDefaultState(), BlockNotifyType.ALL);
                        world.setBlockState(delta.up(), (FloatingRuins.harderDungeons ? Blocks.BEDROCK.getDefaultState() : Blocks.STONEBRICK.getDefaultState()), BlockNotifyType.ALL);
                    }

                    if ((y >= 1) && ((Math.abs(x) == width) ^ (Math.abs(z) == width)))
                        world.setBlockState(delta, block, BlockNotifyType.ALL);

                    if ((y > 0) && (y < height) && (Math.abs(z) < width) && (Math.abs(x) < width))
                        world.setBlockToAir(delta);

                    if ((y == -1) || (y == 0))
                        world.setBlockState(delta, Blocks.STONEBRICK.getDefaultState(), BlockNotifyType.ALL);

                    if (y < -1)
                    {
                        int yg = CommonUtils.getHighestGroundBlock(world, delta);
                        if (((Math.abs(x) == width) || (Math.abs(z) == width)) && !world.isBlockNormalCube(delta, false) && (yg < (y + pos.getY())) && (yg >= (pos.getY() - height)))
                            world.setBlockState(delta, Blocks.STONEBRICK.getDefaultState(), BlockNotifyType.ALL);
                    }
                }
    }

    private void setIgloo(World world, BlockPos pos, int range, IBlockState block)
    {
        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++)
                {
                    int dist = (int) Math.round(Math.sqrt(CommonUtils.sqr(x) + CommonUtils.sqr(y) + CommonUtils.sqr(z)));
                    if (dist <= range)
                    {
                        BlockPos delta = pos.add(new BlockPos(x, y, z));
                        if (y >= 0)
                        {
                            if (dist == range)
                                world.setBlockState(delta, (FloatingRuins.harderDungeons && (y > 2) ? Blocks.BEDROCK.getDefaultState() : block), BlockNotifyType.ALL);

                            // For wolves
                            if ((y == 0) && (dist < range))
                                world.setBlockState(delta, Blocks.GRASS.getDefaultState(), BlockNotifyType.ALL);

                            if ((y > 0) && (dist < range))
                            {
                                world.setBlockToAir(delta);
                                if (y == 1)
                                    world.setBlockState(delta, Blocks.SNOW_LAYER.getDefaultState(), BlockNotifyType.ALL);
                            }
                        }
                        else
                        {
                            if (y == -1)
                                world.setBlockState(delta, block, BlockNotifyType.ALL);

                            int yg = CommonUtils.getHighestGroundBlock(world, delta);
                            if ((dist == range) && !world.isBlockNormalCube(delta, false) && (yg < (y + pos.getY())) && (yg >= (pos.getY() - range)))
                                world.setBlockState(delta, block, BlockNotifyType.ALL);
                        }
                    }
                }
    }

    private void setPyramid(World world, BlockPos pos, int range, IBlockState block)
    {
        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++)
                {
                    BlockPos delta = pos.add(new BlockPos(x, y, z));
                    if (y >= 0)
                    {
                        if (((Math.abs(x) == (range - y)) && (Math.abs(x) >= Math.abs(z))) || ((Math.abs(z) == (range - y)) && (Math.abs(z) >= Math.abs(x))) || (y == 0))
                            world.setBlockState(delta, (FloatingRuins.harderDungeons && (y > 2) ? Blocks.BEDROCK.getDefaultState() : block), BlockNotifyType.ALL);
                        else if (((Math.abs(x) < (range - y)) && (Math.abs(x) >= Math.abs(z))) || ((Math.abs(z) < (range - y)) && (Math.abs(z) >= Math.abs(x))))
                            world.setBlockToAir(delta);
                    }
                    else
                    {
                        if (y == -1)
                            world.setBlockState(delta, block, BlockNotifyType.ALL);

                        int yg = CommonUtils.getHighestGroundBlock(world, delta);
                        if (((Math.abs(x) == range) || (Math.abs(z) == range)) && !world.isBlockNormalCube(delta, false) && (yg < (y + pos.getY())) && (yg >= (pos.getY() - range)))
                            world.setBlockState(delta, block, BlockNotifyType.ALL);
                    }
                }
    }

    static
    {
        helmWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.LEATHER_HELMET, 1, 0), 3));
        helmWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.IRON_HELMET, 1, 0), 7));
        helmWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.CHAINMAIL_HELMET, 1, 0), 9));
        helmWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.GOLDEN_HELMET, 1, 0), 12));
        helmWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.DIAMOND_HELMET, 1, 0), 16));

        plateWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.LEATHER_CHESTPLATE, 1, 0), 3));
        plateWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.IRON_CHESTPLATE, 1, 0), 7));
        plateWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1, 0), 9));
        plateWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.GOLDEN_CHESTPLATE, 1, 0), 12));
        plateWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.DIAMOND_CHESTPLATE, 1, 0), 16));

        leggingWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.LEATHER_LEGGINGS, 1, 0), 3));
        leggingWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.IRON_LEGGINGS, 1, 0), 7));
        leggingWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.CHAINMAIL_LEGGINGS, 1, 0), 9));
        leggingWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.GOLDEN_LEGGINGS, 1, 0), 12));
        leggingWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.DIAMOND_LEGGINGS, 1, 0), 16));

        bootWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.LEATHER_BOOTS, 1, 0), 3));
        bootWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.IRON_BOOTS, 1, 0), 7));
        bootWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.CHAINMAIL_BOOTS, 1, 0), 9));
        bootWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.GOLDEN_BOOTS, 1, 0), 12));
        bootWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.DIAMOND_BOOTS, 1, 0), 16));

        skelWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.BOW, 1, 0), 46));
        skelWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.IRON_SWORD, 1, 0), 11));
        skelWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.DIAMOND_SWORD, 1, 0), 2));
        skelWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.GOLDEN_SWORD, 1, 0), 5));

        zombWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.STONE_SWORD, 1, 0), 10));
        zombWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.DIAMOND_SWORD, 1, 0), 4));
        zombWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.GOLDEN_SWORD, 1, 0), 10));
        zombWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.WOODEN_SWORD, 1, 0), 5));
        zombWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.IRON_SWORD, 1, 0), 11));

        allowedCtgys = new String[] {
            // ChestGenHooks.BONUS_CHEST,
            ChestGenHooks.DUNGEON_CHEST,
            ChestGenHooks.MINESHAFT_CORRIDOR,
            ChestGenHooks.PYRAMID_DESERT_CHEST,
            ChestGenHooks.PYRAMID_JUNGLE_CHEST,
            // ChestGenHooks.PYRAMID_JUNGLE_DISPENSER,
            // ChestGenHooks.STRONGHOLD_CORRIDOR,
            // ChestGenHooks.STRONGHOLD_CROSSING,
            ChestGenHooks.STRONGHOLD_LIBRARY,
            ChestGenHooks.VILLAGE_BLACKSMITH
        };
    }
}
