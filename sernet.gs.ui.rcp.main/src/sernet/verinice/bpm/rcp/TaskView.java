/*******************************************************************************
 * Copyright (c) 2010 Daniel Murygin.
 *
 * This program is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public License 
 * as published by the Free Software Foundation, either version 3 
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,    
 * but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. 
 * If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Daniel Murygin <dm[at]sernet[dot]de> - initial API and implementation
 ******************************************************************************/
package sernet.verinice.bpm.rcp;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import sernet.gs.service.NumericStringComparator;
import sernet.gs.service.RetrieveInfo;
import sernet.gs.ui.rcp.main.Activator;
import sernet.gs.ui.rcp.main.ExceptionUtil;
import sernet.gs.ui.rcp.main.ImageCache;
import sernet.gs.ui.rcp.main.bsi.editors.EditorFactory;
import sernet.gs.ui.rcp.main.bsi.views.HtmlWriter;
import sernet.gs.ui.rcp.main.service.ServiceFactory;
import sernet.hui.common.VeriniceContext;
import sernet.springclient.RightsServiceClient;
import sernet.verinice.bpm.TaskLoader;
import sernet.verinice.interfaces.ActionRightIDs;
import sernet.verinice.interfaces.ICommandService;
import sernet.verinice.interfaces.IInternalServerStartListener;
import sernet.verinice.interfaces.InternalServerEvent;
import sernet.verinice.interfaces.bpm.ITask;
import sernet.verinice.interfaces.bpm.ITaskListener;
import sernet.verinice.interfaces.bpm.ITaskService;
import sernet.verinice.interfaces.bpm.KeyMessage;
import sernet.verinice.interfaces.bpm.KeyValue;
import sernet.verinice.iso27k.rcp.ComboModel;
import sernet.verinice.iso27k.rcp.ComboModelLabelProvider;
import sernet.verinice.iso27k.rcp.Iso27kPerspective;
import sernet.verinice.model.bpm.TaskInformation;
import sernet.verinice.model.common.CnATreeElement;
import sernet.verinice.model.common.configuration.Configuration;
import sernet.verinice.rcp.IAttachedToPerspective;
import sernet.verinice.rcp.RightsEnabledView;
import sernet.verinice.service.commands.LoadAncestors;

/**
 * RCP view to show task loaded by instances of {@link ITaskService}.
 * 
 * New tasks are loaded by a {@link ITaskListener} registered at
 * {@link TaskLoader}.
 * 
 * Double clicking a task opens {@link CnATreeElement} in an editor. View
 * toolbar provides a button to complete tasks.
 * 
 * @see TaskViewDataLoader
 * @author Daniel Murygin <dm[at]sernet[dot]de>
 */
public class TaskView extends RightsEnabledView implements IAttachedToPerspective, IPartListener2 {
    
    private static final Logger LOG = Logger.getLogger(TaskView.class);
    static final NumericStringComparator NSC = new NumericStringComparator();
    
    public static final String ID = "sernet.verinice.bpm.rcp.TaskView"; //$NON-NLS-1$
    
    private static final int WEIGHT_50 = 50;
    private static final int WEIGHT_75 = 75;
    private static final int WEIGHT_25 = 25;
    private static final int WIDTH_4 = 4;
    
    CnATreeElement selectedGroup;
    String selectedAssignee;
    KeyMessage selectedProcessType;
    KeyMessage selectedTaskType;
    
    private TableViewer tableViewer;
    private TaskTableSorter tableSorter = new TaskTableSorter();
    private TaskLabelProvider labelProvider;
    private TaskContentProvider contentProvider;    
    private Browser textPanel;
    Date dueDateFrom = null;
    Date dueDateTo = null;
    Button searchButton;

    private TaskViewDataLoader dataLoader;
    
    ComboModel<CnATreeElement> comboModelGroup;
    Combo comboGroup;    
    ComboModel<Configuration> comboModelAccount;
    Combo comboAccount;
    
    ComboModel<KeyMessage> comboModelProcessType;
    Combo comboProcessType;
    ComboModelTaskType comboModelTaskType;
    Combo comboTaskType;
    
    private Action refreshAction;
    private Action doubleClickAction;
    private Action cancelTaskAction;
    
    private ICommandService commandService;
    private RightsServiceClient rightsService;
    private ITaskListener taskListener;

    public TaskView() {
        super();
        dataLoader = new TaskViewDataLoader(this);
    }

    /* (non-Javadoc)
     * @see sernet.verinice.rcp.RightsEnabledView#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override   
    public void createPartControl(Composite parent) {
        try {
            super.createPartControl(parent);
            initView(parent);
        } catch (Exception e) {
           LOG.error("Error while creating task view.", e); //$NON-NLS-1$
           ExceptionUtil.log(e, Messages.TaskView_5);
        }
    }

    private void initView(Composite parent) {
        createRootComposite(parent);
        dataLoader.initData();
        makeActions();
        addActions();
        addListener();
    }
    
    public void loadTasks() {
        dataLoader.loadTasks();
    }

    private void createRootComposite(Composite parent) {
        SashForm rootSashComposite = createSashComposite(parent, SWT.VERTICAL);      
        SashForm upperSashComposite = createSashComposite(rootSashComposite, SWT.HORIZONTAL);   
        createTablePanel(rootSashComposite);
        
        createInfoPanel(upperSashComposite);
        Composite searchComposite = createSearchComposite(upperSashComposite);
        createSearchForm(searchComposite);
   
        rootSashComposite.setWeights(new int[] { WEIGHT_50, WEIGHT_50 });
        upperSashComposite.setWeights(new int[] { WEIGHT_75, WEIGHT_25 });
    }
    
    private void createTablePanel(Composite parent) {
        this.tableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        this.tableViewer.getControl().setLayoutData(gridData);
        this.tableViewer.setUseHashlookup(true);

        getTable().setHeaderVisible(true);
        getTable().setLinesVisible(true);
        
        createTableColumn(null, 0);
        createTableColumn(Messages.TaskView_9, 1);
        createTableColumn(Messages.TaskView_10, 2);
        createTableColumn(Messages.TaskView_4, 3);
        createTableColumn(Messages.TaskViewColumn_1, 4);
        createTableColumn(Messages.TaskViewColumn_2, 5);
        createTableColumn(Messages.TaskViewColumn_3, 6);

        // set initial column widths
        TableLayout layout = new TableLayout();
        layout.addColumnData(new ColumnWeightData(22, 22, false));
        layout.addColumnData(new ColumnWeightData(192, 192, true));
        layout.addColumnData(new ColumnWeightData(350, 350, true));
        layout.addColumnData(new ColumnWeightData(130, 130, true));
        layout.addColumnData(new ColumnWeightData(138, 138, true));
        layout.addColumnData(new ColumnWeightData(82, 82, true));
        layout.addColumnData(new ColumnWeightData(86, 86, true));

        getTable().setLayout(layout);

        for (TableColumn tc : getTable().getColumns()) {
            tc.pack();
        }

        getTable().addListener(SWT.Expand, getCollapseExpandListener());
        getTable().addListener(SWT.Collapse, getCollapseExpandListener());

        this.contentProvider = new TaskContentProvider(tableViewer);
        this.tableViewer.setContentProvider(this.contentProvider);
        this.labelProvider = new TaskLabelProvider();
        this.tableViewer.setLabelProvider(labelProvider);
        this.tableViewer.setSorter(tableSorter);
    }

    private void createInfoPanel(Composite container) {
        final int gridDataHeight = 80;
        textPanel = new Browser(container, SWT.NONE);
        textPanel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
        final GridData gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
        gridData.heightHint = gridDataHeight;
        textPanel.setLayoutData(gridData);
    }
    
    private void createSearchForm(Composite searchComposite) {
        // Group
        createGroupControls(searchComposite);
        
        // Assignee
        createAssigneeControls(searchComposite);    
        
        // Process
        createProcessTypeControls(searchComposite);
        
        // Task type
        createTaskTypeControls(searchComposite);
        
        // Due date
        createDueDateControls(searchComposite);
        
        // Load button
        createButtonControls(searchComposite);
    }

    private void createGroupControls(Composite searchComposite) {
        Label label = new Label(searchComposite, SWT.WRAP);
        label.setText(Messages.TaskView_11);      
        comboModelGroup = new ComboModel<CnATreeElement>(new GroupLabelProvider());       
        comboGroup = createComboBox(searchComposite);      
        comboGroup.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                comboModelGroup.setSelectedIndex(comboGroup.getSelectionIndex());
                selectedGroup = comboModelGroup.getSelectedObject();
            }
        });
    }

    private void createAssigneeControls(Composite searchComposite) {
        Label label;
        label = new Label(searchComposite, SWT.WRAP);
        label.setText(Messages.TaskView_12);      
        comboModelAccount = new ComboModel<Configuration>(new ComboModelLabelProvider<Configuration>() {
            @Override
            public String getLabel(Configuration account) {
                return account.getUser();
            }
        });  
        comboAccount = createComboBox(searchComposite);     
        comboAccount.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                comboModelAccount.setSelectedIndex(comboAccount.getSelectionIndex());
                Configuration account = comboModelAccount.getSelectedObject();  
                if(account!=null) {
                    selectedAssignee = account.getUser();
                } else {
                    selectedAssignee = null;
                }
            }
        });
    }

    private void createProcessTypeControls(Composite searchComposite) {
        Label label;
        label = new Label(searchComposite, SWT.WRAP);
        label.setText(Messages.TaskView_13);       
        comboModelProcessType = new ComboModel<KeyMessage>(new ComboModelLabelProvider<KeyMessage>() {
            @Override
            public String getLabel(KeyMessage object) {
                return object.getValue();
            }
        });  
        comboProcessType = createComboBox(searchComposite);       
        comboProcessType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                comboModelProcessType.setSelectedIndex(comboProcessType.getSelectionIndex());
                selectedProcessType = comboModelProcessType.getSelectedObject();              
            }
        });
    }

    private void createTaskTypeControls(Composite searchComposite) {
        Label label;
        label = new Label(searchComposite, SWT.WRAP);
        label.setText(Messages.TaskView_14);       
        comboModelTaskType = new ComboModelTaskType();     
        comboTaskType = createComboBox(searchComposite);        
        comboTaskType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                comboModelTaskType.setSelectedIndex(comboTaskType.getSelectionIndex());
                selectedTaskType = comboModelTaskType.getSelectedObject();              
            }
        });
    }

    private void createDueDateControls(Composite searchComposite) {
        Label label = new Label(searchComposite, SWT.WRAP);
        label.setText(Messages.TaskView_15);       
        Composite dateComposite = create2ColumnComposite(searchComposite);      
        final DateTime dueDate = new DateTime(dateComposite, SWT.DATE | SWT.DROP_DOWN);
        dueDate.addSelectionListener (new SelectionAdapter () {
            @Override
            public void widgetSelected (SelectionEvent e) {
                dueDateFrom = extractDateFrom(dueDate);
                dueDateTo = extractDateTo(dueDate);
            }
        });
        dueDate.setEnabled(false);
        final Button enableDateButton = new Button(dateComposite, SWT.TOGGLE);
        enableDateButton.setText("< X"); //$NON-NLS-1$
        enableDateButton.setSelection(true);
        enableDateButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                dueDate.setEnabled(!dueDate.isEnabled());
                if(!dueDate.isEnabled()) {
                    dueDateFrom = null;
                    dueDateTo = null;
                } else {
                    dueDateFrom = extractDateFrom(dueDate);
                    dueDateTo = extractDateTo(dueDate);
                }
                enableDateButton.setSelection(!dueDate.isEnabled());
            }
        });
    }

    private void createButtonControls(Composite searchComposite) {
        Composite buttonComposite = create2ColumnComposite(searchComposite);
        searchButton = new Button(buttonComposite, SWT.NONE);
        searchButton.setText(Messages.TaskView_17);
        searchButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                dataLoader.loadTasks();
            }
        });
    } 

    private SashForm createSashComposite(Composite parent, int orientation) {
        SashForm container = new SashForm(parent, orientation);
        
        container.setSashWidth(WIDTH_4);
        container.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_GRAY));
        return container;
    }
    
    private Composite createSearchComposite(Composite composite) {
        Composite comboComposite = new Composite(composite, SWT.NONE);
        GridData gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
        comboComposite.setLayoutData(gridData);
        GridLayout gridLayout = new GridLayout(1, true);
        gridLayout.marginHeight = 5;
        gridLayout.marginWidth = 5;
        comboComposite.setLayout(gridLayout);
        return comboComposite;
    }
    
    private Composite create2ColumnComposite(Composite composite) {
        Composite comboComposite = new Composite(composite, SWT.NONE);
        GridData gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
        comboComposite.setLayoutData(gridData);
        GridLayout gridLayout = new GridLayout(2, true);
        comboComposite.setLayout(gridLayout);
        return comboComposite;
    }

    private Date extractDateFrom(DateTime dueDate) {
        Calendar cal = getDateWithoutTime(dueDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    private Date extractDateTo(DateTime dueDate) {
        Calendar cal = getDateWithoutTime(dueDate);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }
    
    private Calendar getDateWithoutTime(DateTime dueDate) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, dueDate.getYear());
        cal.set(Calendar.MONTH, dueDate.getMonth());
        cal.set(Calendar.DATE, dueDate.getDay());
        return cal;
    }
    
    private Listener getCollapseExpandListener() {
        Listener listener = new Listener() {

            @Override
            public void handleEvent(Event e) {
                final TreeItem treeItem = (TreeItem) e.item;
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        for (TreeColumn tc : treeItem.getParent().getColumns()) {
                            tc.pack();
                        }
                    }
                });
            }
        };
        return listener;
    }

    private void makeActions() {
        refreshAction = new Action() {
            @Override
            public void run() {
                dataLoader.loadTasks();
            }
        };
        refreshAction.setText(Messages.ButtonRefresh);
        refreshAction.setImageDescriptor(ImageCache.getInstance().getImageDescriptor(ImageCache.RELOAD));

        doubleClickAction = new Action() {
            @Override
            public void run() {
                if (getViewer().getSelection() instanceof IStructuredSelection && ((IStructuredSelection) getViewer().getSelection()).getFirstElement() instanceof TaskInformation) {
                    try {
                        TaskInformation task = (TaskInformation) ((IStructuredSelection) getViewer().getSelection()).getFirstElement();
                        RetrieveInfo ri = RetrieveInfo.getPropertyInstance();
                        LoadAncestors loadControl = new LoadAncestors(task.getElementType(), task.getUuid(), ri);
                        loadControl = getCommandService().executeCommand(loadControl);
                        if (loadControl.getElement() != null) {
                            EditorFactory.getInstance().updateAndOpenObject(loadControl.getElement());
                        } else {
                            showError("Error", Messages.TaskView_25); //$NON-NLS-1$
                        }
                    } catch (Exception t) {
                        LOG.error("Error while opening control.", t); //$NON-NLS-1$
                    }
                }
            }
        };
        
       
        cancelTaskAction = new Action(Messages.ButtonCancel, SWT.TOGGLE) {
            @Override
            public void run() {
                try {
                    cancelTask();
                    this.setChecked(false);
                } catch (Exception e) {
                    LOG.error("Error while canceling task.", e); //$NON-NLS-1$
                    showError(Messages.TaskView_6, Messages.TaskView_7);
                }
            }
        };
        cancelTaskAction.setEnabled(false);
        cancelTaskAction.setImageDescriptor(ImageCache.getInstance().getImageDescriptor(ImageCache.MASSNAHMEN_UMSETZUNG_NEIN));

        if (Activator.getDefault().isStandalone() && !Activator.getDefault().getInternalServer().isRunning()) {
            IInternalServerStartListener listener = new IInternalServerStartListener() {
                @Override
                public void statusChanged(InternalServerEvent e) {
                    if (e.isStarted()) {
                        configureActions();
                    }
                }

            };
            Activator.getDefault().getInternalServer().addInternalServerStatusListener(listener);
        } else {
            configureActions();
        }
    }

    private void configureActions() {
        cancelTaskAction.setEnabled(getRightsService().isEnabled(ActionRightIDs.TASKDELETE));
        boolean taskShowAllEnabled = getRightsService().isEnabled(ActionRightIDs.TASKSHOWALL);
        comboAccount.setEnabled(taskShowAllEnabled);
    }

    private void addActions() {
        addToolBarActions();
        getViewer().addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                doubleClickAction.run();
            }
        });
    }

    private void addToolBarActions() {
        IActionBars bars = getViewSite().getActionBars();
        IToolBarManager manager = bars.getToolBarManager();
        // Dummy action to force displaying the toolbar in a new line
        Action dummyAction = new Action() {
        };
        dummyAction.setText(" "); //$NON-NLS-1$
        dummyAction.setEnabled(false);
        ActionContributionItem item = new ActionContributionItem(dummyAction);
        item.setMode(ActionContributionItem.MODE_FORCE_TEXT);
        manager.add(item);
        manager.add(this.refreshAction);
        manager.add(cancelTaskAction);
    }

    private void addListener() {
        taskListener = new ITaskListener() {
            @Override
            public void newTasks(List<ITask> taskList) {
                addTasks(taskList);
            }

            @Override
            public void newTasks() {
                Display.getDefault().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        dataLoader.loadTasks();
                    }
                });
            }
        };
        TaskChangeRegistry.addTaskChangeListener(taskListener);
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                if (isTaskSelected()) {
                    try {
                        taskSelected();
                    } catch (Exception t) {
                        LOG.error("Error while configuring task actions.", t); //$NON-NLS-1$
                    }
                } else {
                    resetToolbar();
                    getInfoPanel().setText(""); //$NON-NLS-1$
                }
                getViewSite().getActionBars().updateActionBars();
            }

            private boolean isTaskSelected() {
                return getViewer().getSelection() instanceof IStructuredSelection && ((IStructuredSelection) getViewer().getSelection()).getFirstElement() instanceof TaskInformation;
            }
        });
        // First we create a menu Manager
        MenuManager menuManager = new MenuManager();
        Menu menu = menuManager.createContextMenu(tableViewer.getTable());
        // Set the MenuManager
        tableViewer.getTable().setMenu(menu);
        getSite().registerContextMenu(menuManager, tableViewer);
        // Make the selection available
        getSite().setSelectionProvider(tableViewer);
        
        
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override  
    public void dispose() {
        TaskChangeRegistry.removeTaskChangeListener(taskListener);
        dataLoader.dispose();
        super.dispose();
    }

    private void taskSelected() {
        IToolBarManager manager = resetToolbar();

        cancelTaskAction.setEnabled(false);
        cancelTaskAction.setEnabled(getRightsService().isEnabled(ActionRightIDs.TASKDELETE));
        TaskInformation task = (TaskInformation) ((IStructuredSelection) getViewer().getSelection()).getFirstElement();
        getInfoPanel().setText(HtmlWriter.getPage(task.getDescription()));

        List<KeyValue> outcomeList = task.getOutcomes();
        for (KeyValue keyValue : outcomeList) {
            CompleteTaskAction completeAction = new CompleteTaskAction(this, keyValue.getKey());
            completeAction.setText(keyValue.getValue());
            completeAction.setImageDescriptor(ImageCache.getInstance().getImageDescriptor(ImageCache.MASSNAHMEN_UMSETZUNG_JA));
            ActionContributionItem item = new ActionContributionItem(completeAction);

            item.setMode(ActionContributionItem.MODE_FORCE_TEXT);
            manager.add(item);
        }
    }

    private IToolBarManager resetToolbar() {
        IToolBarManager manager = getViewSite().getActionBars().getToolBarManager();
        manager.removeAll();

        addToolBarActions();
        return manager;
    }

    /**
     * @param taskList
     */
    protected void addTasks(List<ITask> taskList) {
        final List<ITask> newList;
        if (taskList == null || taskList.isEmpty()) {
            newList = new LinkedList<ITask>();
        } else {
            newList = taskList;
        }
        List<ITask> currentTaskList = (List<ITask>) getViewer().getInput();
        if (currentTaskList != null) {
            for (ITask task : currentTaskList) {
                if (!newList.contains(task)) {
                    newList.add(task);
                }
            }
        }
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                getViewer().setInput(newList);
            }
        });
    }

    private void cancelTask() throws InvocationTargetException, InterruptedException {
        IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
        final StructuredSelection selection = (StructuredSelection) getViewer().getSelection();
        final int number = selection.size();
        if (number > 0 && MessageDialog.openConfirm(getShell(), Messages.ConfirmTaskDelete_0, Messages.bind(Messages.ConfirmTaskDelete_1, selection.size()))) {
            progressService.run(true, true, new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    Activator.inheritVeriniceContextState();
                    for (Iterator iterator = selection.iterator(); iterator.hasNext();) {
                        Object sel = iterator.next();
                        if (sel instanceof TaskInformation) {
                            TaskInformation task = (TaskInformation) sel;
                            ServiceFactory.lookupTaskService().cancelTask(task.getId());
                            TaskView.this.contentProvider.removeTask(task);
                        }
                    }
                }
            });
            getInfoPanel().setText(""); //$NON-NLS-1$
            showInformation(Messages.TaskView_0, NLS.bind(Messages.TaskView_8, number));
        }
    } 

    public void removeTask(ITask task) {
        contentProvider.removeTask(task);
    }
  
    private Combo createComboBox(Composite composite) {
        Combo combo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return combo;
    }
    
    private void createTableColumn(String label, int columnIndex) {
        TableColumn column;
        column = new TableColumn(getTable(), SWT.LEFT);
        if(label!=null) {
            column.setText(label);
        }
        column.addSelectionListener(new TaskSortSelectionAdapter(this, column, columnIndex));
    }
    
    protected TableViewer getViewer() {
        return tableViewer;
    }
    
    protected TaskTableSorter getTableSorter() {
        return tableSorter;
    }

    Browser getInfoPanel() {
        return textPanel;
    }

    private Table getTable() {
        return this.tableViewer.getTable();
    }
    
    protected void showError(final String title, final String message) {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                MessageDialog.openError(getShell(), title, message);
            }
        });
    }

    protected void showInformation(final String title, final String message) {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                MessageDialog.openInformation(getShell(), title, message);
            }
        });
    }

    static Display getDisplay() {
        Display display = Display.getCurrent();
        // may be null if outside the UI thread
        if (display == null) {
            display = Display.getDefault();
        }
        return display;
    }
    
    private static Shell getShell() {
        return getDisplay().getActiveShell();
    }
    
    public ICommandService getCommandService() {
        if (commandService == null) {
            commandService = ServiceFactory.lookupCommandService();
        }
        return commandService;
    }
    
    public RightsServiceClient getRightsService() {
        if (rightsService == null) {
            rightsService = (RightsServiceClient) VeriniceContext.get(VeriniceContext.RIGHTS_SERVICE);
        }
        return rightsService;
    }
    
    /* (non-Javadoc)
     * @see sernet.verinice.rcp.RightsEnabledView#setFocus()
     */
    @Override
    public void setFocus() {
        // empty
    }
   
    /* (non-Javadoc)
     * @see sernet.verinice.rcp.IAttachedToPerspective#getPerspectiveId()
     */
    public String getPerspectiveId() {
        return Iso27kPerspective.ID;
    }
    
    /* (non-Javadoc)
     * @see sernet.verinice.rcp.RightsEnabledView#getRightID()
     */
    @Override
    public String getRightID() {
        return ActionRightIDs.TASKVIEW;
    }
    
    /* (non-Javadoc)
     * @see sernet.verinice.rcp.RightsEnabledView#getViewId()
     */
    @Override
    public String getViewId() {
        return ID;
    }
 
}