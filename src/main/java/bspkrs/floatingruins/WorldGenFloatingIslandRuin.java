package bspkrs.floatingruins;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.ChestGenHooks;
import bspkrs.util.BlockNotifyType;
import bspkrs.util.CommonUtils;
import cpw.mods.fml.common.registry.GameData;

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
    private boolean                                                 isLavaNearby;
    
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
    public boolean generate(World world, Random random, int x, int y, int z)
    {
        setDungeon(world, random, x, y, z);
        return true;
    }
    
    private void setDungeon(World world, Random random, int x, int y, int z)
    {
        BiomeGenBase biomegenbase = world.getWorldChunkManager().getBiomeGenAt(x, z);
        Block dungeonBlock = getDungeonBlock(biomegenbase);
        if (dungeonBlock.equals(Blocks.snow))
            setIgloo(world, x, y, z, 5, dungeonBlock);
        else if (dungeonBlock.equals(Blocks.sandstone))
            setPyramid(world, x, y, z, 6, dungeonBlock);
        else if (dungeonBlock.equals(Blocks.red_mushroom_block))
            CommonUtils.setHugeMushroom(world, random, x, y, z, dungeonBlock);
        else
            setBox(world, random, x, y, z, 4, 4, getDungeonBlock(biomegenbase));
        
        setChest(world, random, x, y, z);
        setSpawner(world, biomegenbase, x, y + 2, z);
    }
    
    private void setChest(World world, Random random, int x, int y, int z)
    {
        world.setBlock(x, y, z, Blocks.chest, 0, BlockNotifyType.ALL);
        TileEntityChest tileentitychest = (TileEntityChest) world.getTileEntity(x, y, z);
        addItems(tileentitychest, random);
        
        Block blockingBlock = (FloatingRuins.harderDungeons ? Blocks.bedrock : Blocks.obsidian);
        
        world.setBlock(x + 1, y, z, blockingBlock, 0, BlockNotifyType.ALL);
        world.setBlock(x - 1, y, z, blockingBlock, 0, BlockNotifyType.ALL);
        world.setBlock(x, y, z + 1, blockingBlock, 0, BlockNotifyType.ALL);
        world.setBlock(x, y, z - 1, blockingBlock, 0, BlockNotifyType.ALL);
        world.setBlock(x, y - 1, z, blockingBlock, 0, BlockNotifyType.ALL);
        
        if (FloatingRuins.harderDungeons)
            world.setBlock(x, y + 1, z, Blocks.obsidian, 0, BlockNotifyType.ALL);
        else
            world.setBlock(x, y + 1, z, Blocks.cobblestone, 0, BlockNotifyType.ALL);
    }
    
    private void setSpawner(World world, BiomeGenBase biomegenbase, int x, int y, int z)
    {
        world.setBlock(x, y, z, Blocks.mob_spawner, 0, BlockNotifyType.ALL);
        
        world.setBlock(x + 1, y, z, Blocks.obsidian, 0, BlockNotifyType.ALL);
        world.setBlock(x - 1, y, z, Blocks.obsidian, 0, BlockNotifyType.ALL);
        world.setBlock(x, y, z + 1, Blocks.obsidian, 0, BlockNotifyType.ALL);
        world.setBlock(x, y, z - 1, Blocks.obsidian, 0, BlockNotifyType.ALL);
        if (FloatingRuins.harderDungeons)
        {
            world.setBlock(x, y - 1, z, Blocks.obsidian, 0, BlockNotifyType.ALL);
            world.setBlock(x, y + 1, z, Blocks.obsidian, 0, BlockNotifyType.ALL);
        }
        
        TileEntityMobSpawner tileEntityMobSpawner = (TileEntityMobSpawner) world.getTileEntity(x, y, z);
        if (tileEntityMobSpawner != null)
        {
            String[] mobIDList;
            if (FloatingRuins.allowMultiMobSpawners)
                mobIDList = getSpawnerMobList(world, biomegenbase);
            else
                mobIDList = new String[] { getSpawnerType(world, biomegenbase, x, z) };
            
            if (mobIDList.length == 1 && mobIDList[0].trim().equalsIgnoreCase("Default"))
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
    
    private Block getDungeonBlock(BiomeGenBase biomegenbase)
    {
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.PLAINS))
            return Blocks.planks;
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SANDY))
            return Blocks.sandstone;
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SNOWY))
            return Blocks.snow;
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.FOREST))
            return Blocks.cobblestone;
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.JUNGLE))
            return Blocks.mossy_cobblestone;
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SWAMP))
            return Blocks.mossy_cobblestone;
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.MOUNTAIN) || BiomeDictionary.isBiomeOfType(biomegenbase, Type.HILLS))
            return Blocks.stone;
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.WATER))
            if (biomegenbase.getClass().getSimpleName().toLowerCase(Locale.US).contains("ocean"))
                return Blocks.stonebrick;
            else
                return Blocks.planks;
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.MUSHROOM))
            return Blocks.red_mushroom_block;
        else
            return Blocks.brick_block;
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
        String id = GameData.getItemRegistry().getNameForObject(Items.egg);
        int size = 1;
        int meta = 0;
        if (itemStack.length > 0)
            id = itemStack[0].trim();
        
        if (itemStack.length > 1)
            size = CommonUtils.parseInt(itemStack[1].trim());
        
        if (itemStack.length > 2)
            meta = CommonUtils.parseInt(itemStack[2].trim());
        
        Item item = GameData.getItemRegistry().getObject(id);
        
        if (item == null)
            item = Items.egg;
        
        if (!item.getHasSubtypes() && meta != 0)
            meta = 0;
        
        return new ItemStack(item, size, meta);
    }
    
    public String getSpawnerType(World world, BiomeGenBase biomegenbase, int x, int z)
    {
        if (isLavaNearby)
            return getMobString(spawnerNearLava, world, x, z);
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.PLAINS))
            return getMobString(spawnerPlains, world, x, z);
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SANDY))
            return getMobString(spawnerDesert, world, x, z);
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SNOWY) && BiomeDictionary.isBiomeOfType(biomegenbase, Type.FOREST))
            return getMobString(spawnerTaiga, world, x, z);
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SNOWY))
            return getMobString(spawnerIceBiomes, world, x, z);
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.FOREST))
            return getMobString(spawnerForest, world, x, z);
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.JUNGLE))
            return getMobString(spawnerJungle, world, x, z);
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.SWAMP))
            return getMobString(spawnerSwampland, world, x, z);
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.MOUNTAIN) || BiomeDictionary.isBiomeOfType(biomegenbase, Type.HILLS))
            return getMobString(spawnerHills, world, x, z);
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.WATER))
            if (biomegenbase.getClass().getSimpleName().toLowerCase(Locale.US).contains("ocean"))
                return getMobString(spawnerOcean, world, x, z);
            else
                return getMobString(spawnerRiver, world, x, z);
        
        if (BiomeDictionary.isBiomeOfType(biomegenbase, Type.MUSHROOM))
            return getMobString(spawnerMushroom, world, x, z);
        else
            return getMobString(spawnerDefault, world, x, z);
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
    
    private String getMobString(String s, World world, int x, int z)
    {
        Random random = new Random();
        if (s.equalsIgnoreCase("default"))
            return getMobString(spawnerDefault, world, x, z);
        else
        {
            String mob = s.split(",")[random.nextInt(s.split(",").length)].trim();
            if (mob.equalsIgnoreCase("slime") && !(new EntitySlime(world)).getCanSpawnHere())
                return getMobString(spawnerDefault, world, x, z);
            
            return mob;
        }
    }
    
    public void setBox(World world, Random random, int xIn, int yIn, int zIn, int width, int height, Block block)
    {
        for (int x = -width; x <= width; x++)
            for (int y = height; y >= -height; y--)
                for (int z = -width; z <= width; z++)
                {
                    if (y == height || (Math.abs(x) == width && Math.abs(z) == width && y >= 0))
                    {
                        world.setBlock(x + xIn, y + yIn, z + zIn, Blocks.stonebrick, 0, BlockNotifyType.ALL);
                        world.setBlock(x + xIn, y + yIn + 1, z + zIn, (FloatingRuins.harderDungeons ? Blocks.bedrock : Blocks.stonebrick), 0, BlockNotifyType.ALL);
                    }
                    
                    if (y >= 1 && ((Math.abs(x) == width) ^ (Math.abs(z) == width)))
                        world.setBlock(x + xIn, y + yIn, z + zIn, block, 0, BlockNotifyType.ALL);
                    
                    if (y > 0 && y < height && Math.abs(z) < width && Math.abs(x) < width)
                        world.setBlockToAir(x + xIn, y + yIn, z + zIn);
                    
                    if (y == -1 || y == 0)
                        world.setBlock(x + xIn, y + yIn, z + zIn, Blocks.stonebrick, 0, BlockNotifyType.ALL);
                    
                    if (y < -1)
                    {
                        int yg = CommonUtils.getHighestGroundBlock(world, x + xIn, y + yIn, z + zIn);
                        if ((Math.abs(x) == width || Math.abs(z) == width) && !world.isBlockNormalCubeDefault(x + xIn, y + yIn, z + zIn, false) && yg < y + yIn && yg >= yIn - height)
                            world.setBlock(x + xIn, y + yIn, z + zIn, Blocks.stonebrick, 0, BlockNotifyType.ALL);
                    }
                }
    }
    
    private void setIgloo(World world, int xIn, int yIn, int zIn, int range, Block block)
    {
        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++)
                {
                    int dist = (int) Math.round(Math.sqrt(CommonUtils.sqr(x) + CommonUtils.sqr(y) + CommonUtils.sqr(z)));
                    if (dist <= range)
                    {
                        if (y >= 0)
                        {
                            if (dist == range)
                                world.setBlock(x + xIn, y + yIn, z + zIn, (FloatingRuins.harderDungeons && y > 2 ? Blocks.bedrock : block), 0, BlockNotifyType.ALL);
                            
                            // For wolves
                            if (y == 0 && dist < range)
                                world.setBlock(x + xIn, y + yIn, z + zIn, Blocks.grass, 0, BlockNotifyType.ALL);
                            
                            if (y > 0 && dist < range)
                            {
                                world.setBlockToAir(x + xIn, y + yIn, z + zIn);
                                if (y == 1)
                                    world.setBlock(x + xIn, y + yIn, z + zIn, Blocks.snow_layer, 0, BlockNotifyType.ALL);
                            }
                        }
                        else
                        {
                            if (y == -1)
                                world.setBlock(x + xIn, yIn - 1, z + zIn, block, 0, BlockNotifyType.ALL);
                            
                            int yg = CommonUtils.getHighestGroundBlock(world, x + xIn, y + yIn, z + zIn);
                            if (dist == range && !world.isBlockNormalCubeDefault(x + xIn, y + yIn, z + zIn, false) && yg < y + yIn && yg >= yIn - range)
                                world.setBlock(x + xIn, y + yIn, z + zIn, block, 0, BlockNotifyType.ALL);
                        }
                    }
                }
    }
    
    private void setPyramid(World world, int xIn, int yIn, int zIn, int range, Block block)
    {
        for (int x = -range; x <= range; x++)
            for (int y = -range; y <= range; y++)
                for (int z = -range; z <= range; z++)
                {
                    if (y >= 0)
                    {
                        if ((Math.abs(x) == range - y && Math.abs(x) >= Math.abs(z)) || (Math.abs(z) == range - y && Math.abs(z) >= Math.abs(x)) || y == 0)
                            world.setBlock(xIn + x, y + yIn, zIn + z, (FloatingRuins.harderDungeons && y > 2 ? Blocks.bedrock : block), 0, BlockNotifyType.ALL);
                        else if ((Math.abs(x) < range - y && Math.abs(x) >= Math.abs(z)) || (Math.abs(z) < range - y && Math.abs(z) >= Math.abs(x)))
                            world.setBlockToAir(xIn + x, y + yIn, zIn + z);
                    }
                    else
                    {
                        if (y == -1)
                            world.setBlock(x + xIn, y + yIn, z + zIn, block, 0, BlockNotifyType.ALL);
                        
                        int yg = CommonUtils.getHighestGroundBlock(world, x + xIn, y + yIn, z + zIn);
                        if ((Math.abs(x) == range || Math.abs(z) == range) && !world.isBlockNormalCubeDefault(x + xIn, y + yIn, z + zIn, false) && yg < y + yIn && yg >= yIn - range)
                            world.setBlock(x + xIn, y + yIn, z + zIn, block, 0, BlockNotifyType.ALL);
                    }
                }
    }
    
    static
    {
        helmWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.leather_helmet, 1, 0), 3));
        helmWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.iron_helmet, 1, 0), 7));
        helmWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.chainmail_helmet, 1, 0), 9));
        helmWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.golden_helmet, 1, 0), 12));
        helmWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.diamond_helmet, 1, 0), 16));
        
        plateWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.leather_chestplate, 1, 0), 3));
        plateWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.iron_chestplate, 1, 0), 7));
        plateWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.chainmail_chestplate, 1, 0), 9));
        plateWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.golden_chestplate, 1, 0), 12));
        plateWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.diamond_chestplate, 1, 0), 16));
        
        leggingWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.leather_leggings, 1, 0), 3));
        leggingWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.iron_leggings, 1, 0), 7));
        leggingWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.chainmail_leggings, 1, 0), 9));
        leggingWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.golden_leggings, 1, 0), 12));
        leggingWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.diamond_leggings, 1, 0), 16));
        
        bootWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.leather_boots, 1, 0), 3));
        bootWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.iron_boots, 1, 0), 7));
        bootWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.chainmail_boots, 1, 0), 9));
        bootWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.golden_boots, 1, 0), 12));
        bootWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.diamond_boots, 1, 0), 16));
        
        skelWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.bow, 1, 0), 46));
        skelWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.iron_sword, 1, 0), 11));
        skelWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.diamond_sword, 1, 0), 2));
        skelWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.golden_sword, 1, 0), 5));
        
        zombWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.stone_sword, 1, 0), 10));
        zombWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.diamond_sword, 1, 0), 4));
        zombWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.golden_sword, 1, 0), 10));
        zombWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.wooden_sword, 1, 0), 5));
        zombWeapWeights.add(new SimpleEntry<ItemStack, Integer>(new ItemStack(Items.iron_sword, 1, 0), 11));
        
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
