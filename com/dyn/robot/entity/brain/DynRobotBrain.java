package com.dyn.robot.entity.brain;

import java.util.ArrayList;
import java.util.Iterator;

import com.dyn.robot.api.IDYNRobotAccess;
import com.dyn.robot.programs.Program;
import com.dyn.robot.programs.ProgramState;
import com.dyn.robot.programs.UserProgramLibrary;
import com.google.common.base.Objects;

import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.turtle.blocks.TileTurtle;
import dan200.computercraft.shared.turtle.core.TurtleBrain;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class DynRobotBrain extends RobotBrain implements IDYNRobotAccess {
	public class BlockChange {
		private BlockPos m_coordinates;
		private IBlockState m_previousState;

		public BlockChange(BlockPos coordinates, IBlockState previousState) {
			m_coordinates = coordinates;
			m_previousState = previousState;
		}

		public BlockPos getCoordinates() {
			return m_coordinates;
		}

		public IBlockState getPreviousState() {
			return m_previousState;
		}
	}

	private boolean m_locked;
	private boolean m_demo;
	private String m_ownerName;
	private UserProgramLibrary m_programLibrary;
	private String m_selectedProgram;
	private ProgramState m_programState;
	private int m_programSlot;
	private BlockPos m_savedPosition;
	private EnumFacing m_savedDirection;
	private int m_savedSlot;
	private ItemStack[] m_savedInventory;

	private ArrayList<BlockChange> m_savedBlockChanges;

	public DynRobotBrain(EntityLiving robot) {
		super(robot);

		m_locked = false;
		m_demo = false;
		m_ownerName = null;
		m_programLibrary = null;
		m_selectedProgram = null;

		m_programState = ProgramState.Stopped;
		m_programSlot = -1;

		m_savedPosition = null;
		m_savedDirection = null;
		m_savedSlot = -1;
		m_savedInventory = null;
		m_savedBlockChanges = new ArrayList();
	}

	@Override
	public void clearSavedState() {
		setSavedState(null, null, -1, null);
	}

	@Override
	public void clearVariable(String variable) {
		ServerComputer computer = getOwner().getServerComputer();
		if (computer != null) {
			computer.getUserData().removeTag("edu_variable_" + variable);
			computer.updateUserData();
		}
	}

	@Override
	public void clearVariables() {
		ServerComputer computer = getOwner().getServerComputer();
		if (computer != null) {
			NBTTagCompound userData = computer.getUserData();
			Iterator it = userData.getKeySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				if (key.startsWith("edu_variable_")) {
					it.remove();
				}
			}
			computer.updateUserData();
		}
	}

	@Override
	public int createProgram(String title) {
		initProgramLibrary();
		if (m_programLibrary != null) {
			int index = m_programLibrary.createProgram(m_programLibrary.findUnusedTitle(title));
			if (index >= 0) {
				updateProgramLibrary();
			}
			return index;
		}
		return -1;
	}

	@Override
	public void deleteProgram(int index) {
		initProgramLibrary();
		if (m_programLibrary != null) {
			int selectedIndex = m_programLibrary.lookupProgramByPath(m_selectedProgram);
			m_programLibrary.deleteProgram(index);
			if (selectedIndex >= m_programLibrary.getSize()) {
				selectProgram(m_programLibrary.getSize() - 1);
			} else if (selectedIndex > index) {
				selectProgram(selectedIndex - 1);
			} else {
				selectProgram(selectedIndex);
			}
			updateProgramLibrary();
		}
	}

	@Override
	public void exportProgramToAllPlayers(int index) {
		// initProgramLibrary();
		// if (this.m_programLibrary != null)
		// {
		// Program program = this.m_programLibrary.getProgram(index);
		// if (program != null)
		// {
		// List<Program> programs = new ArrayList(32);
		// programs.add(program);
		// // program.getDependencies(programs, this.m_programLibrary);
		//
		// MinecraftServer server = MinecraftServer.getServer();
		// if (server != null)
		// {
		// List players = server.getConfigurationManager().playerEntityList;
		// for (int i = 0; i < players.size(); i++)
		// {
		// EntityPlayer player = (EntityPlayer)players.get(i);
		//
		// World world = player.getEntityWorld();
		// int diskID = ComputerCraft.createUniqueNumberedSaveDir(world,
		// "computer/export");
		// IWritableMount mount = ComputerCraftAPI.createSaveDirMount(world,
		// "computer/export/" + diskID, ComputerCraft.floppySpaceLimit);
		// for (int j = 0; j < programs.size(); j++)
		// {
		// Program dependency = (Program)programs.get(j);
		// dependency.saveAs(mount, Integer.toString(j));
		// }
		// ItemStack stack = new
		// ItemStack(ComputerCraft.Items.disk);//.create(diskID,
		// program.getTitle());
		// ItemStack remainder = InventoryUtil.storeItems(stack,
		// player.inventory, 0, 36, 0);
		// if (remainder != null) {
		// WorldUtil.dropItemStack(remainder, player.getEntityWorld(),
		// player.posX, player.posY + 1.5D, player.posZ);
		// }
		// }
		// }
		// }
		// }
	}

	// this is a nice feature but we might have to think of a better way to
	// handle this
	@Override
	public void exportProgramToPlayerInventory(int index, EntityPlayer player) {
		// initProgramLibrary();
		// if (this.m_programLibrary != null)
		// {
		// Program program = this.m_programLibrary.getProgram(index);
		// if (program != null)
		// {
		// List<Program> programs = new ArrayList(32);
		// programs.add(program);
		// //program.getDependencies(programs, this.m_programLibrary);
		//
		// World world = getWorld();
		// int diskID = ComputerCraft.createUniqueNumberedSaveDir(world,
		// "computer/export");
		// IWritableMount mount = ComputerCraftAPI.createSaveDirMount(world,
		// "computer/export/" + diskID, ComputerCraft.floppySpaceLimit);
		// for (int i = 0; i < programs.size(); i++)
		// {
		// Program dependency = (Program)programs.get(i);
		// dependency.saveAs(mount, Integer.toString(i));
		// }
		// ItemStack stack = dynrobot.Items.exportedProgramDisk.create(diskID,
		// program.getTitle());
		// ItemStack remainder = InventoryUtil.storeItems(stack,
		// player.inventory, 0, 36, 0);
		// if (remainder != null) {
		// WorldUtil.dropItemStack(remainder, player.getEntityWorld(),
		// player.posX, player.posY + 1.5D, player.posZ);
		// }
		// }
		// }
	}

	@Override
	public int getFuelLimit() {
		// TODO: this will probably serve as our RAM metaphor
		return 100;
	}

	@Override
	public String getOwnerName() {
		return m_ownerName;
	}

	@Override
	public Program getProgram() {
		initProgramLibrary();
		if (m_programLibrary != null) {
			int index = m_programLibrary.lookupProgramByPath(m_selectedProgram);
			if (index >= 0) {
				return m_programLibrary.getProgram(index);
			}
		}
		return null;
	}

	@Override
	public UserProgramLibrary getProgramLibrary() {
		initProgramLibrary();
		return m_programLibrary;
	}

	@Override
	public int getProgramSlot() {
		return m_programSlot;
	}

	@Override
	public ProgramState getProgramState() {
		return m_programState;
	}

	@Override
	public ArrayList<BlockChange> getSavedBlockChanges() {
		return m_savedBlockChanges;
	}

	@Override
	public EnumFacing getSavedDirection() {
		return m_savedDirection;
	}

	@Override
	public ItemStack[] getSavedInventory() {
		return m_savedInventory;
	}

	@Override
	public BlockPos getSavedPosition() {
		return m_savedPosition;
	}

	@Override
	public int getSavedSlot() {
		return m_savedSlot;
	}

	@Override
	public int getSelectedProgramIndex() {
		initProgramLibrary();
		if (m_programLibrary != null) {
			return m_programLibrary.lookupProgramByPath(m_selectedProgram);
		}
		return -1;
	}

	private void initProgramLibrary() {
		World world = getWorld();
		if (world != null) {
			if ((m_ownerName != null)
					&& ((m_programLibrary == null) || (!m_programLibrary.getUserName().equals(m_ownerName)))) {
				m_programLibrary = UserProgramLibrary.getLibrary(getWorld(), m_ownerName);
				if (m_programLibrary.lookupProgramByPath(m_selectedProgram) < 0) {
					m_selectedProgram = (m_programLibrary.getSize() > 0 ? m_programLibrary.getProgram(0).getPath()
							: null);
				}
				updateProgramLibrary();
				getOwner().onInventoryDefinitelyChanged();
			} else if ((m_ownerName == null) && (m_programLibrary != null)) {
				m_programLibrary = null;
				m_selectedProgram = null;
				updateProgramLibrary();
				getOwner().onInventoryDefinitelyChanged();
			}
		}
	}

	@Override
	public boolean isDemoTurtle() {
		return m_demo;
	}

	@Override
	public boolean isFuelNeeded() {
		// TODO: this needs to be toggled probably
		return false;
	}

	@Override
	public boolean isLocked() {
		return m_locked;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		if (nbttagcompound.hasKey("locked")) {
			m_locked = nbttagcompound.getBoolean("locked");
		} else {
			m_locked = false;
		}
		if (nbttagcompound.hasKey("demo")) {
			m_demo = nbttagcompound.getBoolean("demo");
		} else {
			m_demo = false;
		}
		if (nbttagcompound.hasKey("ownerName")) {
			setOwnerNameInternal(nbttagcompound.getString("ownerName"));
		} else {
			setOwnerNameInternal(null);
		}
		if (nbttagcompound.hasKey("selectedProgram")) {
			m_selectedProgram = Integer.toString(nbttagcompound.getInteger("selectedProgram"));
		} else {
			m_selectedProgram = null;
		}
	}

	@Override
	public void renameProgram(int index, String newTitle) {
		initProgramLibrary();
		if (m_programLibrary != null) {
			Program program = m_programLibrary.getProgram(index);
			if ((program != null) && (!program.getTitle().equals(newTitle))) {
				program.setTitle(m_programLibrary.findUnusedTitle(newTitle));
				program.save();
				updateProgramLibrary();
			}
		}
	}

	@Override
	public boolean saveBlockChange(BlockPos coordinates, IBlockState previousState) {
		if (m_savedPosition != null) {
			m_savedBlockChanges.add(new BlockChange(coordinates, previousState));
			return true;
		}
		return false;
	}

	@Override
	public void selectProgram(int index) {
		initProgramLibrary();
		if (m_programLibrary != null) {
			if ((index >= -1) && (index < m_programLibrary.getSize())) {
				m_selectedProgram = m_programLibrary.getProgram(index).getPath();
				getOwner().onInventoryDefinitelyChanged();
			}
		}
	}

	@Override
	public void setDemoTurtle(boolean demo) {
		m_demo = demo;
	}

	@Override
	public void setLocked(boolean locked) {
		m_locked = locked;
	}

	@Override
	public void setOwnerName(String name) {
		if (setOwnerNameInternal(name)) {
			updateProgramLibrary();
			getOwner().onInventoryDefinitelyChanged();
		}
	}

	private boolean setOwnerNameInternal(String name) {
		if (!Objects.equal(m_ownerName, name)) {
			m_ownerName = name;
			return true;
		}
		return false;
	}

	private void setProgramError(String message) {
		ServerComputer computer = getOwner().getServerComputer();
		if (computer != null) {
			String oldMessage = null;
			if (computer.getUserData().hasKey("edu_errorMessage")) {
				oldMessage = computer.getUserData().getString("edu_errorMessage");
			}
			if (!Objects.equal(oldMessage, message)) {
				if (message != null) {
					computer.getUserData().setString("edu_errorMessage", message);
				} else {
					computer.getUserData().removeTag("edu_errorMessage");
				}
				computer.updateUserData();
			}
		}
	}

	@Override
	public void setProgramErrored(String error) {
		m_programState = ProgramState.Errored;
		setProgramError(error);
	}

	@Override
	public void setProgramPaused() {
		m_programState = ProgramState.Paused;
		setProgramError(null);
	}

	@Override
	public void setProgramRunning() {
		m_programState = ProgramState.Running;
		setProgramError(null);
	}

	@Override
	public void setProgramSlot(int slot) {
		m_programSlot = slot;
	}

	@Override
	public void setProgramStopped() {
		m_programState = ProgramState.Stopped;
		setProgramError(null);
	}

	@Override
	public void setSavedState(BlockPos position, EnumFacing direction, int slot, ItemStack[] savedInventory) {
		if ((position != null) && (direction != null) && (slot >= 0)) {
			m_savedPosition = position;
			m_savedDirection = direction;
			m_savedSlot = slot;
			m_savedInventory = savedInventory;
			m_savedBlockChanges.clear();
		} else {
			m_savedPosition = null;
			m_savedDirection = null;
			m_savedSlot = -1;
			m_savedInventory = null;
			m_savedBlockChanges.clear();
		}
		getOwner().onTileEntityChange();
	}

	@Override
	public void setupComputer(ServerComputer computer) {
		super.setupComputer(computer);
		initProgramLibrary();
		updateProgramLibrary(computer);
	}

	@Override
	public void setVariable(String variable, String value) {
		ServerComputer computer = getOwner().getServerComputer();
		if (computer != null) {
			computer.getUserData().setString("edu_variable_" + variable, value);
			computer.updateUserData();
		}
	}

	@Override
	public void update() {
		super.update();
		updateProgramLibrary();
	}

	public void updateProgramLibrary() {
		TileTurtle owner = getOwner();
		if (owner != null) {
			updateProgramLibrary(owner.getServerComputer());
		}
	}

	private void updateProgramLibrary(ServerComputer computer) {
		if (computer != null) {
			NBTTagCompound userData = computer.getUserData();
			NBTTagCompound oldUserData = (NBTTagCompound) userData.copy();
			if (m_ownerName != null) {
				userData.setString("edu_owner", m_ownerName);
			} else {
				userData.removeTag("edu_owner");
			}
			if (m_programLibrary != null) {
				NBTTagList libraryList = new NBTTagList();
				for (int i = 0; i < m_programLibrary.getSize(); i++) {
					NBTTagCompound entry = new NBTTagCompound();
					Program program = m_programLibrary.getProgram(i);
					entry.setString("title", program.getTitle());
					libraryList.appendTag(entry);
				}
				userData.setTag("edu_library", libraryList);
			} else {
				userData.removeTag("edu_library");
			}
			if (!userData.equals(oldUserData)) {
				computer.updateUserData();
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setBoolean("locked", m_locked);
		nbttagcompound.setBoolean("demo", m_demo);
		if (m_ownerName != null) {
			nbttagcompound.setString("ownerName", m_ownerName);
		}
		if (m_selectedProgram != null) {
			nbttagcompound.setInteger("selectedProgram", Integer.parseInt(m_selectedProgram));
		}
	}
}
