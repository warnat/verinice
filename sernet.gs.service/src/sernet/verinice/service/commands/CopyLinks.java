package sernet.verinice.service.commands;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import sernet.verinice.interfaces.CommandException;
import sernet.verinice.interfaces.ICommandService;
import sernet.verinice.interfaces.IPostProcessor;

/**
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 * 
 */
@SuppressWarnings("serial")
public class CopyLinks implements IPostProcessor, Serializable {

    private static final Logger logger = Logger.getLogger(CopyLinks.class);

    /*
     * @see sernet.verinice.iso27k.service.PasteService.IPostProcessor#process(
     * java.util.Map)
     */
    @Override
    public void process(ICommandService commandService, final List<String> copyUuidList,
            final Map<String, String> sourceDestMap) {
        try {
            final CopyLinksCommand copyLinksCommand = new CopyLinksCommand(sourceDestMap);
            commandService.executeCommand(copyLinksCommand);
        } catch (final CommandException e) {
            logger.error("Error while copying links on server.", e);
        }
    }

}