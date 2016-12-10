package de.paxii.clarinet.event.events.block;

import de.paxii.clarinet.event.events.Event;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import lombok.Value;

/**
 * Created by Lars on 29.03.2016.
 */
@Value
public class BlockBreakEvent implements Event {
  private Block block;
  private BlockPos blockPos;

  public BlockBreakEvent(Block block, BlockPos blockPos) {
    this.block = block;
    this.blockPos = blockPos;
  }
}
