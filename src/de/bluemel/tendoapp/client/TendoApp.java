package de.bluemel.tendoapp.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

import de.bluemel.tendoapp.shared.Seminar;
import de.bluemel.tendoapp.shared.SeminarDTO;
import de.bluemel.tendoapp.shared.TendoAppService;
import de.bluemel.tendoapp.shared.TendoAppServiceAsync;
import de.bluemel.tendoapp.shared.TimeLogic;
import de.bluemel.tendoapp.shared.Umlaut;

/**
 * Entry point class.
 */
public class TendoApp implements EntryPoint {

	private TendoApp thisApp = null;

	private TendoAppServiceAsync service = null;

	private CellTable<Seminar> seminarTableView = null;

	private ListDataProvider<Seminar> seminarTableModel = null;

	private SingleSelectionModel<Seminar> seminarTableSelectionModel = null;

	private TextArea messageArea = null;

	private Button enterButton = null;

	private Button modifyButton = null;

	private Button removeButton = null;

	private Button removeOutdatedButton = null;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		thisApp = this;

		try {
			this.service = GWT.create(TendoAppService.class);
		} catch (RuntimeException e) {
			new MessageBox("Exception", new String[] { e.getClass().getName(), e.getMessage() }).show();
		}

		this.seminarTableView = initTable();
		this.seminarTableModel = new ListDataProvider<Seminar>();
		this.seminarTableModel.addDataDisplay(this.seminarTableView);
		this.seminarTableSelectionModel = new SingleSelectionModel<Seminar>();
		this.seminarTableView.setSelectionModel(this.seminarTableSelectionModel);
		this.messageArea = new TextArea();
		this.messageArea.setWidth(Integer.toString(Window.getClientWidth() - 30) + " px");
		this.enterButton = initEnterButton();
		this.modifyButton = initModifyButton();
		this.removeButton = initRemoveButton();
		this.removeOutdatedButton = initRemoveOutdatedButton();
		RootPanel.get("seminarDateTable").add(this.seminarTableView);
		RootPanel.get("messageArea").add(this.messageArea);
		RootPanel.get("enterButton").add(this.enterButton);
		RootPanel.get("modifyButton").add(this.modifyButton);
		RootPanel.get("removeButton").add(this.removeButton);
		RootPanel.get("removeOutdatedButton").add(this.removeOutdatedButton);
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				messageArea.setWidth(Integer.toString(Window.getClientWidth() - 30) + " px");
			}
		});
		// removeOutdatedSeminars();
		readAllSeminars();
	}

	private Button initEnterButton() {
		Button button = new Button("Lehrgang eingeben");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new EnterSeminarDialog(thisApp, service).show();
			}
		});
		return button;
	}

	private Button initModifyButton() {
		Button button = new Button("Lehrgang " + Umlaut.auml + "ndern");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (getSelectedSeminar() != null) {
					new ModifySeminarDialog(thisApp, service, getSelectedSeminar()).show();
				} else {
					new MessageBox("Fehler", "Kein Lehrgang ausgew" + Umlaut.auml + "hlt").show();
				}
			}
		});
		return button;
	}

	private Button initRemoveButton() {
		Button button = new Button("Lehrgang entfernen");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (getSelectedSeminar() != null) {
					new RemoveSeminarDialog(thisApp, service, getSelectedSeminar()).show();
				} else {
					new MessageBox("Fehler", "Kein Lehrgang ausgew" + Umlaut.auml + "hlt").show();
				}
			}
		});
		return button;
	}

	private Button initRemoveOutdatedButton() {
		Button button = new Button("Alte Lehrg" + Umlaut.auml + "nge entfernen");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeOutdatedSeminars();
			}
		});
		return button;
	}

	private CellTable<Seminar> initTable() {
		final CellTable<Seminar> seminarDateTable = new CellTable<Seminar>();
		final Column<Seminar, SafeHtml> colFirstDay = new Column<Seminar, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Seminar seminar) {
				final SafeHtmlBuilder sb = new SafeHtmlBuilder();
				if (retrieveOverlappingSeminars(seminar).size() > 0) {
					sb.appendHtmlConstant("<div style=\"background-color:#FFCCCC\">"
							+ TimeLogic.format(seminar.getFirstDay()) + "</div>");
				} else {
					sb.appendHtmlConstant(TimeLogic.format(seminar.getFirstDay()));
				}
				return sb.toSafeHtml();
			}
		};
		final Column<Seminar, SafeHtml> colLastDay = new Column<Seminar, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Seminar seminar) {
				final SafeHtmlBuilder sb = new SafeHtmlBuilder();
				if (retrieveOverlappingSeminars(seminar).size() > 0) {
					// System.out.println("@@@ Seminar " + seminar.toString() +
					// " overlaps with "
					// + retrieveOverlappingSeminars(seminar).get(0));
					sb.appendHtmlConstant("<div style=\"background-color:#FFCCCC\">"
							+ TimeLogic.format(seminar.getLastDay()) + "</div>");
				} else {
					sb.appendHtmlConstant(TimeLogic.format(seminar.getLastDay()));
				}
				return sb.toSafeHtml();
			}
		};
		final TextColumn<Seminar> colTitle = new TextColumn<Seminar>() {
			@Override
			public String getValue(Seminar seminar) {
				return seminar.getTitle();
			}
		};
		final TextColumn<Seminar> colInstructor = new TextColumn<Seminar>() {
			@Override
			public String getValue(Seminar seminar) {
				return seminar.getInstructor();
			}
		};
		final TextColumn<Seminar> colOrganizer = new TextColumn<Seminar>() {
			@Override
			public String getValue(Seminar seminar) {
				return seminar.getOrganizer();
			}
		};
		final TextColumn<Seminar> colLocation = new TextColumn<Seminar>() {
			@Override
			public String getValue(Seminar seminar) {
				return seminar.getLocation();
			}
		};
		final Column<Seminar, SafeHtml> colAnnouncement = new Column<Seminar, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Seminar seminar) {
				final SafeHtmlBuilder sb = new SafeHtmlBuilder();
				if (seminar.getAnnouncement() != null && seminar.getAnnouncement().length() > 0) {
					if (seminar.getAnnouncement().startsWith("http://") || seminar.getAnnouncement().startsWith("http://")) {
						final String iconFileName = getIconFileName(seminar.getAnnouncement());
						if (iconFileName != null) {
							sb.appendHtmlConstant("<a href=\"" + seminar.getAnnouncement()
									+ "\"><img width=\"30\" src=\"" + iconFileName + "\" border=\"0\" alt=\"Ausschreibung (PDF)\"></a>");
						} else {
							sb.appendHtmlConstant("<a href=\"" + seminar.getAnnouncement()
									+ "\">" + seminar.getAnnouncement() + "</a>");
						}
					} else {
						sb.appendHtmlConstant(seminar.getAnnouncement());
					}
				}
				return sb.toSafeHtml();
			}

			private String getIconFileName(final String announcement) {
				String iconFileName = null;
				if (announcement.endsWith(".pdf")) {
					iconFileName = "pdf.png";
				} else if (announcement.endsWith(".jpg")) {
					iconFileName = "jpg.png";
				}
				return iconFileName;
			}
		};

		seminarDateTable.addColumn(colFirstDay, "Von");
		seminarDateTable.addColumn(colLastDay, "Bis");
		seminarDateTable.addColumn(colTitle, "Titel");
		seminarDateTable.addColumn(colInstructor, "Leiter");
		seminarDateTable.addColumn(colOrganizer, "Ausrichter");
		seminarDateTable.addColumn(colLocation, "Ort");
		seminarDateTable.addColumn(colAnnouncement, "Ausschreibung");
		return seminarDateTable;
	}

	private Seminar getSelectedSeminar() {
		return this.seminarTableSelectionModel.getSelectedObject();
	}

	public void readAllSeminars() {
		this.messageArea.setText("Lehrg" + Umlaut.auml + "nge werden eingelesen...");
		seminarTableModel.getList().clear();
		seminarTableModel.refresh();
		seminarTableView.redraw();
		this.service.findAllSeminars(new AsyncCallback<List<SeminarDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				messageArea.setText("Fehler beim Einlesen der Lehrg" + Umlaut.auml + "nge!");
				new MessageBox("Fehler beim Einlesen der Lehrg" + Umlaut.auml + "nge!", caught.getMessage()).show();
			}

			@Override
			public void onSuccess(List<SeminarDTO> result) {
				for (final SeminarDTO seminar : result) {
					seminarTableModel.getList().add(seminar);
				}
				seminarTableModel.refresh();
				messageArea.setText("");
			}
		});
	}

	public void removeOutdatedSeminars() {
		this.messageArea.setText("Alte Lehrg" + Umlaut.auml + "nge werden entfernt...");
		this.service.removeOutdatedSeminars(new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				messageArea.setText("Fehler beim Entfernen alter Lehrg" + Umlaut.auml + "nge!");
				new MessageBox("Fehler beim Entfernen alter Lehrg" + Umlaut.auml + "nge!", caught.getMessage()).show();
			}

			@Override
			public void onSuccess(Integer result) {
				switch (result.intValue()) {
				case 0:
					messageArea.setText("Derzeit kein alter Lehrgang vorhanden.");
					break;
				case 1:
					readAllSeminars();
					messageArea.setText("1 Lehrgang wurde entfernt.");
					break;
				default:
					readAllSeminars();
					messageArea.setText(result.toString() + " Lehrg" + Umlaut.auml + "nge wurden entfernt.");
					break;
				}
			}
		});
	}

	public List<Seminar> getSeminars() {
		return this.seminarTableModel.getList();
	}

	public List<Seminar> retrieveOverlappingSeminars(final Seminar seminar) {
		List<Seminar> overlappingSeminars = new ArrayList<Seminar>();
		for (final Seminar currentSeminar : getSeminars()) {
			if (TimeLogic.dateRangesOverlap(seminar.getFirstDay(), seminar.getLastDay(), currentSeminar.getFirstDay(),
					currentSeminar.getLastDay())
					&& (seminar.getKey() == null || !seminar.getKey().equals(currentSeminar.getKey()))) {
				overlappingSeminars.add(currentSeminar);
			}
		}
		return overlappingSeminars;
	}
}
