package fi.dy.masa.litematica.gui.widgets;

import net.minecraft.client.renderer.GlStateManager;
import fi.dy.masa.litematica.scheduler.ITask;
import fi.dy.masa.litematica.scheduler.TaskScheduler;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.StringUtils;

public class WidgetTaskEntry extends WidgetListEntryBase<ITask>
{
    private final WidgetListTasks parent;
    private final boolean isOdd;

    public WidgetTaskEntry(int x, int y, int width, int height, boolean isOdd,
            ITask task, int listIndex, WidgetListTasks parent)
    {
        super(x, y, width, height, task, listIndex);

        this.parent = parent;
        this.isOdd = isOdd;

        int posX = x + width;
        ButtonListener listener = new ButtonListener(ButtonListener.Type.REMOVE, this);
        this.addButton(new ButtonGeneric(posX, y + 1, -1, true, StringUtils.translate("litematica.gui.button.remove")), listener);
    }

    @Override
    public void render(int mouseX, int mouseY, boolean isActiveGui, int hoveredWidgetId)
    {
        RenderUtils.color(1f, 1f, 1f, 1f);

        int x = this.getX();
        int y = this.getY();
        int z = this.getZLevel();
        int width = this.getWidth();
        int height = this.getHeight();

        // Draw a lighter background for the hovered entry
        if (this.isMouseOver(mouseX, mouseY))
        {
            RenderUtils.drawRect(x, y, width, height, 0x70FFFFFF, z);
        }
        else if (this.isOdd)
        {
            RenderUtils.drawRect(x, y, width, height, 0x20FFFFFF, z);
        }
        // Draw a slightly lighter background for even entries
        else
        {
            RenderUtils.drawRect(x, y, width, height, 0x50FFFFFF, z);
        }

        String name = this.getEntry().getDisplayName();
        this.drawString(x + 4, y + this.getCenteredTextOffsetY(), 0xFFFFFFFF, name);

        this.drawSubWidgets(mouseX, mouseY, isActiveGui, hoveredWidgetId);

        RenderUtils.disableItemLighting();
        GlStateManager.disableLighting();
    }

    private static class ButtonListener implements IButtonActionListener
    {
        private final Type type;
        private final WidgetTaskEntry widget;

        public ButtonListener(Type type, WidgetTaskEntry widget)
        {
            this.type = type;
            this.widget = widget;
        }

        @Override
        public void actionPerformedWithButton(ButtonBase button, int mouseButton)
        {
            if (this.type == Type.REMOVE)
            {
                ITask task = this.widget.getEntry();

                if (TaskScheduler.getInstanceClient().removeTask(task) == false)
                {
                    TaskScheduler.getInstanceServer().removeTask(task);
                }

                this.widget.parent.refreshEntries();
            }
        }

        public enum Type
        {
            REMOVE;
        }
    }
}
