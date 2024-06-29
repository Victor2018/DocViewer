
package com.nvqquy98.lib.doc.office.fc.fs.storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nvqquy98.lib.doc.office.fc.fs.filesystem.BlockSize;
import com.nvqquy98.lib.doc.office.fc.fs.filesystem.CFBConstants;

/**
 * This class manages and creates the Block Allocation Table, which is
 * basically a set of linked lists of block indices.
 * <P>
 * Each block of the filesystem has an index. The first block, the
 * header, is skipped; the first block after the header is index 0,
 * the next is index 1, and so on.
 * <P>
 * A block's index is also its index into the Block Allocation
 * Table. The entry that it finds in the Block Allocation Table is the
 * index of the next block in the linked list of blocks making up a
 * file, or it is set to -2: end of list.
 *
 */
public final class BlockAllocationTableReader
{

    /**
     * Maximum number size (in blocks) of the allocation table as supported by
     * POI.<br/>
     *
     * This constant has been chosen to help POI identify corrupted data in the
     * header block (rather than crash immediately with {@link OutOfMemoryError}
     * ). It's not clear if the compound document format actually specifies any
     * upper limits. For files with 512 byte blocks, having an allocation table
     * of 65,335 blocks would correspond to a total file size of 4GB. Needless
     * to say, POI probably cannot handle files anywhere near that size.
     */
    private final IntList _entries;
    private BlockSize bigBlockSize;
    
    /**
     * create a BlockAllocationTableReader for an existing filesystem. Side
     * effect: when this method finishes, the BAT blocks will have
     * been removed from the raw block list, and any blocks labeled as
     * 'unused' in the block allocation table will also have been
     * removed from the raw block list.
     *
     * @param block_count the number of BAT blocks making up the block
     *                    allocation table
     * @param block_array the array of BAT block indices from the
     *                    filesystem's header
     * @param xbat_count the number of XBAT blocks
     * @param xbat_index the index of the first XBAT block
     * @param raw_block_list the list of RawDataBlocks
     *
     * @exception IOException if, in trying to create the table, we
     *            encounter logic errors
     */
    public BlockAllocationTableReader(BlockSize bigBlockSize, int block_count,
        int[] block_array, int xbat_count, int xbat_index, BlockList raw_block_list)
        throws IOException
    {
        this.bigBlockSize = bigBlockSize;
        _entries = new IntList();
        int limit = Math.min(block_count, block_array.length);
        int block_index;

        // This will hold all of the BAT blocks in order
        RawDataBlock blocks[] = new RawDataBlock[block_count];
        // Process the first (up to) 109 BAT blocks
        for (block_index = 0; block_index < limit; block_index++)
        {
            // Check that the sector number of the BAT block is a valid one
            int nextOffset = block_array[block_index];
            if (nextOffset > raw_block_list.blockCount())
            {
                throw new IOException("Your file contains " + raw_block_list.blockCount()
                    + " sectors, but the initial DIFAT array at index " + block_index
                    + " referenced block # " + nextOffset + ". This isn't allowed and "
                    + " your file is corrupt");
            }
            // Record the sector number of this BAT block 
            blocks[block_index] = (RawDataBlock)raw_block_list.remove(nextOffset);
        }

        // Process additional BAT blocks via the XBATs
        if (block_index < block_count)
        {

            // must have extended blocks
            if (xbat_index < 0)
            {
                throw new IOException(
                    "BAT count exceeds limit, yet XBAT index indicates no valid entries");
            }
            int chain_index = xbat_index;
            int max_entries_per_block = bigBlockSize.getXBATEntriesPerBlock();
            int chain_index_offset = bigBlockSize.getNextXBATChainOffset();

            // Each XBAT block contains either:
            //  (maximum number of sector indexes) + index of next XBAT
            //  some sector indexes + FREE sectors to max # + EndOfChain
            for (int j = 0; j < xbat_count; j++)
            {
                limit = Math.min(block_count - block_index, max_entries_per_block);
                byte[] data = raw_block_list.remove(chain_index).getData();
                int offset = 0;

                for (int k = 0; k < limit; k++)
                {
                    blocks[block_index++] = (RawDataBlock)raw_block_list.remove(LittleEndian.getInt(data, offset));
                    offset += LittleEndian.INT_SIZE;
                }
                chain_index = LittleEndian.getInt(data, chain_index_offset);
                if (chain_index == CFBConstants.END_OF_CHAIN)
                {
                    break;
                }
            }
        }
        if (block_index != block_count)
        {
            throw new IOException("Could not find all blocks");
        }

        // Now that we have all of the raw data blocks which make
        //  up the FAT, go through and create the indices
        setEntries(blocks, raw_block_list);
    }
    
    /**
     * create a BlockAllocationTableReader from an array of raw data blocks
     *
     * @param blocks the raw data
     * @param raw_block_list the list holding the managed blocks
     *
     * @exception IOException
     */
    public BlockAllocationTableReader(BlockSize bigBlockSize, RawDataBlock[] blocks,
        BlockList raw_block_list) throws IOException
    {
        this.bigBlockSize = bigBlockSize;
        _entries = new IntList();
        setEntries(blocks, raw_block_list);
    }

    /**
     * walk the entries from a specified point and return the
     * associated blocks. The associated blocks are removed from the
     * block list
     *
     * @param startBlock the first block in the chain
     * @param blockList the raw data block list
     *
     * @return array of ListManagedBlocks, in their correct order
     *
     * @exception IOException if there is a problem acquiring the blocks
     */
    public RawDataBlock[] fetchBlocks(int startBlock, int headerPropertiesStartBlock,
        BlockList blockList) throws IOException
    {
        List<RawDataBlock> blocks = new ArrayList<RawDataBlock>();
        int currentBlock = startBlock;
        boolean firstPass = true;
        RawDataBlock dataBlock = null;

        // Process the chain from the start to the end
        // Normally we have header, data, end
        // Sometimes we have data, header, end
        // For those cases, stop at the header, not the end
        while (currentBlock != CFBConstants.END_OF_CHAIN)
        {
            try
            {
                // Grab the data at the current block offset
                dataBlock = blockList.remove(currentBlock);
                blocks.add(dataBlock);
                // Now figure out which block we go to next
                currentBlock = _entries.get(currentBlock);
                firstPass = false;
            }
            catch(IOException e)
            {
                if (currentBlock == headerPropertiesStartBlock)
                {
                    currentBlock = CFBConstants.END_OF_CHAIN;
                }
                else if (currentBlock == 0 && firstPass)
                {
                    currentBlock = CFBConstants.END_OF_CHAIN;
                }
                else
                {
                    // Ripple up
                    throw e;
                }
            }
        }

        return blocks.toArray(new RawDataBlock[blocks.size()]);
    }

    /**
     * Convert an array of blocks into a set of integer indices
     *
     * @param blocks the array of blocks containing the indices
     * @param raw_blocks the list of blocks being managed. Unused
     *                   blocks will be eliminated from the list
     */
    private void setEntries(RawDataBlock[] blocks, BlockList raw_blocks) throws IOException
    {
        int limit = bigBlockSize.getBATEntriesPerBlock();

        for (int block_index = 0; block_index < blocks.length; block_index++)
        {
            byte[] data = blocks[block_index].getData();
            int offset = 0;
            for (int k = 0; k < limit; k++)
            {
                int entry = LittleEndian.getInt(data, offset);

                if (entry == CFBConstants.UNUSED_BLOCK)
                {
                    raw_blocks.zap(_entries.size());
                }
                _entries.add(entry);
                offset += LittleEndian.INT_SIZE;
            }

            // discard block
            blocks[block_index] = null;
        }
        raw_blocks.setBAT(this);
    }
}
