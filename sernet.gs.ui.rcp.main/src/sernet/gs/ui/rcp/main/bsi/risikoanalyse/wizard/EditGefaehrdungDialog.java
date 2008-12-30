package sernet.gs.ui.rcp.main.bsi.risikoanalyse.wizard;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control; 
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import sernet.gs.model.Gefaehrdung;
import sernet.gs.ui.rcp.main.ExceptionUtil;
import sernet.gs.ui.rcp.main.bsi.risikoanalyse.model.OwnGefaehrdung;
import sernet.gs.ui.rcp.main.bsi.risikoanalyse.model.OwnGefaehrdungHome;

/**
 * Dialog to edit an exsisting OwnGefaehrdung.
 * 
 * @author ahanekop@sernet.de
 */
public class EditGefaehrdungDialog extends Dialog {

	private Text textNumber;
	private Text textName;
	private Text textDescription;
	private Combo textCategory;
	private OwnGefaehrdung ownGefaehrdung;
	
	/**
	 * Constructor.
	 * 
	 * @param parentShell shell of parent Composite
	 * @param newOwnGefaehrdung the OwnGefaehrdung to edit
	 */
	public EditGefaehrdungDialog(Shell parentShell, OwnGefaehrdung newOwnGefaehrdung) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		ownGefaehrdung = newOwnGefaehrdung;
	}

	/**
	 * Creates the content area of the Dialog.
	 * 
	 * @param parent the parent Composite
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		composite.setLayout(gridLayout);
		
		/* label number */
		final Label labelNumber = new Label(composite, SWT.NONE);
		GridData gridLabelNumber = new GridData();
		gridLabelNumber.horizontalAlignment = SWT.LEFT;
	    gridLabelNumber.verticalAlignment = SWT.CENTER;
	    labelNumber.setText("Nummer:");
		labelNumber.setLayoutData(gridLabelNumber);
		
		/* text number */
		textNumber = new Text(composite, SWT.BORDER);
		GridData gridTextNumber = new GridData();
		gridTextNumber.horizontalAlignment = SWT.FILL;
	    gridTextNumber.verticalAlignment = SWT.CENTER;
	    gridTextNumber.grabExcessHorizontalSpace = true;
		textNumber.setLayoutData(gridTextNumber);
		textNumber.setText(ownGefaehrdung.getId());
		
		/* label name */
		final Label labelName = new Label(composite, SWT.NONE);
		GridData gridLabelName = new GridData();
		gridLabelName.horizontalAlignment = SWT.LEFT;
	    gridLabelName.verticalAlignment = SWT.CENTER;
	    labelName.setText("Name:");
		labelName.setLayoutData(gridLabelName);
		
		/* text name */
		textName = new Text(composite, SWT.BORDER);
		GridData gridTextName = new GridData();
		gridTextName.horizontalAlignment = SWT.FILL;
	    gridTextName.verticalAlignment = SWT.CENTER;
	    gridTextName.grabExcessHorizontalSpace = true;
		textName.setLayoutData(gridTextName);
		textName.setText(ownGefaehrdung.getTitel());
		
		/* label description */
		final Label labelDescription = new Label(composite, SWT.NONE);
		GridData gridLabelDescription = new GridData();
		gridLabelDescription.horizontalAlignment = SWT.LEFT;
	    gridLabelDescription.verticalAlignment = SWT.TOP;
	    labelDescription.setText("Beschreibung:");
		labelDescription.setLayoutData(gridLabelDescription);
		
		/* text description */
		textDescription = new Text(composite, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI | SWT.WRAP);
		GridData gridTextDescription = new GridData();
		gridTextDescription.horizontalAlignment = SWT.FILL;
	    gridTextDescription.verticalAlignment = SWT.FILL;
	    gridTextDescription.grabExcessHorizontalSpace = true;
	    gridTextDescription.grabExcessVerticalSpace = true;
	    gridTextDescription.widthHint = 400;
	    gridTextDescription.heightHint = 200;
		textDescription.setLayoutData(gridTextDescription);
		textDescription.setText(ownGefaehrdung.getBeschreibung());
		
		/* label category */
		final Label labelCategory = new Label(composite, SWT.NONE);
		GridData gridLabelCategory = new GridData();
		gridLabelCategory.horizontalAlignment = SWT.LEFT;
	    gridLabelCategory.verticalAlignment = SWT.TOP;
	    labelCategory.setText("Kategorie:");
		labelCategory.setLayoutData(gridLabelCategory);
		
		/* text category */
		textCategory = new Combo(composite, SWT.DROP_DOWN);
		GridData gridTextCategory = new GridData();
		gridTextCategory.horizontalAlignment = SWT.FILL;
		gridTextCategory.verticalAlignment = SWT.CENTER;
		gridTextCategory.grabExcessHorizontalSpace = true;
		textCategory.setLayoutData(gridTextCategory);
		textCategory.setItems(loadCategories());
		textCategory.setText(ownGefaehrdung.getOwnkategorie());
		
		 return composite;
	}

	/**
	 * Loads all categories for OwnGefaehrdungen from database.
	 * 
	 * @return an array of all categories as Strings
	 */
	private String[] loadCategories() {
		ArrayList<String> allCategories =  new ArrayList<String> ();
		allCategories.add("[neue Kategorie]");
		allCategories.add(Gefaehrdung.KAT_STRING_HOEHERE_GEWALT);
		allCategories.add(Gefaehrdung.KAT_STRING_ORG_MANGEL);
		allCategories.add(Gefaehrdung.KAT_STRING_MENSCH);
		allCategories.add(Gefaehrdung.KAT_STRING_TECHNIK);
		allCategories.add(Gefaehrdung.KAT_STRING_VORSATZ);

		List<OwnGefaehrdung> allOwnGefaehrdungen = OwnGefaehrdungHome.getInstance().loadAll();
		Boolean contains = false;
		
		for (OwnGefaehrdung gefaehrdung : allOwnGefaehrdungen) {
			for (String category : allCategories) {
				if (category.equalsIgnoreCase(gefaehrdung.getKategorieAsString())) {
					/* category already in List */
					contains = true;
					break;
				}
			}
			if (!contains) {
				allCategories.add(gefaehrdung.getKategorieAsString());
			} else {
				contains = false;
			}
		}
		return allCategories.toArray(new String[allCategories.size()]);
	}

	/**
	 * Saves the OwnGefaehrung in the database, if okay button
	 * is pressed.
	 */
	@Override
	protected void okPressed() {
		ownGefaehrdung.setId(textNumber.getText());
		ownGefaehrdung.setTitel(textName.getText());
		ownGefaehrdung.setBeschreibung(textDescription.getText());
		ownGefaehrdung.setOwnkategorie(textCategory.getText());
		
		try {
			OwnGefaehrdungHome.getInstance().save(ownGefaehrdung);
		} catch (Exception e) {
			ExceptionUtil.log(e, "Änderung konnte nicht gespeichert werden.");
		}
		
		super.okPressed();
	}
}
