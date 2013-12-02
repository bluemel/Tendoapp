package de.bluemel.tendoapp.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.bluemel.tendoapp.shared.Seminar;
import de.bluemel.tendoapp.shared.SeminarDTO;
import de.bluemel.tendoapp.shared.TendoAppServiceAsync;
import de.bluemel.tendoapp.shared.TimeLogic;
import de.bluemel.tendoapp.shared.Umlaut;

public class EnterSeminarDialog extends DialogBox {

	// private static final String TEXTBOX_WIDTH = "300 px";

	private static final long DAY_IN_MILLIS = 24 * 60 * 60 * 1000;

	private TendoApp app = null;

	protected Seminar givenSeminar = null;

	protected TendoAppServiceAsync service;

	protected TextBox tbFirstDay = new TextBox();

	protected TextBox tbLastDay = new TextBox();

	protected TextBox tbTitle = new TextBox();

	protected TextBox tbInstructor = new TextBox();

	protected TextBox tbOrganizer = new TextBox();

	protected TextBox tbLocation = new TextBox();

	protected TextBox tbAnnouncement = new TextBox();

	protected Button okButton;

	public EnterSeminarDialog(final TendoApp app, final TendoAppServiceAsync service) {
		this(app, service, null, "Eingabe eines neuen geplanten Lehrgangs");
	}

	public EnterSeminarDialog(final TendoApp app, final TendoAppServiceAsync service, final Seminar givenSeminar,
			final String title) {
		this.app = app;
		this.service = service;
		this.givenSeminar = givenSeminar;
		setText(title);
		setTitle(title);
		setAnimationEnabled(true);

		if (givenSeminar != null) {
			this.tbFirstDay.setText(TimeLogic.format(givenSeminar.getFirstDay()));
			this.tbLastDay.setText(TimeLogic.format(givenSeminar.getLastDay()));
			this.tbTitle.setText(givenSeminar.getTitle());
			this.tbInstructor.setText(givenSeminar.getInstructor());
			this.tbOrganizer.setText(givenSeminar.getOrganizer());
			this.tbLocation.setText(givenSeminar.getLocation());
			this.tbAnnouncement.setText(givenSeminar.getAnnouncement());
		}
		this.tbFirstDay.setTitle("Von");
		//this.tbFirstDay.setWidth(TEXTBOX_WIDTH);
		this.tbLastDay.setTitle("Bis");
		//this.tbLastDay.setWidth(TEXTBOX_WIDTH);
		this.tbTitle.setTitle("Titel");
		this.tbTitle.setText("Aikidolehrgang");
		//this.tbTitle.setWidth(TEXTBOX_WIDTH);
		this.tbInstructor.setTitle("Leiter");
		//this.tbInstructor.setWidth(TEXTBOX_WIDTH);
		this.tbLocation.setTitle("Ort");
		//this.tbLocation.setWidth(TEXTBOX_WIDTH);
		this.tbOrganizer.setTitle("Ausrichter");
		//this.tbOrganizer.setWidth(TEXTBOX_WIDTH);
		this.tbAnnouncement.setTitle("Ausschreibung");
		//this.tbAnnouncement.setWidth(TEXTBOX_WIDTH);

		// We can set the id of a widget by accessing its Element

		VerticalPanel labelPanel = new VerticalPanel();
		//labelPanel.setWidth("50 px");
		labelPanel.addStyleName("labelPanel");
		labelPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		labelPanel.setSpacing(17);

		labelPanel.add(new Label("Von (erster Tag):"));
		labelPanel.add(new Label("Bis (letzter Tag):"));
		labelPanel.add(new Label("Titel:"));
		labelPanel.add(new Label("Leiter:"));
		labelPanel.add(new Label("Ausrichter:"));
		labelPanel.add(new Label("Ort:"));
		labelPanel.add(new Label("Ausschreibung:"));

		VerticalPanel textPanel = new VerticalPanel();
		textPanel.addStyleName("textPanel");
		textPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		textPanel.setSpacing(5);
		textPanel.add(tbFirstDay);
		textPanel.add(tbLastDay);
		textPanel.add(tbTitle);
		textPanel.add(tbInstructor);
		textPanel.add(tbOrganizer);
		textPanel.add(tbLocation);
		textPanel.add(tbAnnouncement);

		HorizontalPanel inputPanel = new HorizontalPanel();
		inputPanel.addStyleName("inputPanel");
		inputPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		inputPanel.add(labelPanel);
		inputPanel.add(textPanel);

		FlowPanel buttonPanel = new FlowPanel();
		buttonPanel.addStyleName("buttonPanel");
		this.okButton = new Button("OK");
		okButton.getElement().setId("okButton");
		//buttonPanel.setCellWidth(okButton, "200 px");
		final Button closeButton = new Button("Abbrechen");
		closeButton.getElement().setId("closeButton");
		//buttonPanel.setCellWidth(closeButton, "200 px");
		buttonPanel.add(okButton);
		buttonPanel.add(closeButton);

		VerticalPanel mainPanel = new VerticalPanel();
		//this.setWidth("1000 px");
		//mainPanel.setWidth("1000 px");
		mainPanel.addStyleName("mainPanel");
		mainPanel.add(inputPanel);
		mainPanel.add(buttonPanel);
		setWidget(mainPanel);

		if (givenSeminar == null) {
			okButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					enterAddSeminar();
				}
			});
		}

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				closeDialog(false);
			}
		});

		center();
	}

	private void enterAddSeminar() {
		try {
			final SeminarDTO seminar = readSeminarFromUI(true);
			addNewSeminar(seminar);
		} catch (OverlappingSeminarsException e) {
			final SeminarDTO seminar = readSeminarFromUI(false);
			final YesNoHandler yesNoHandler = new YesNoHandler() {

				@Override
				public void onYes() {
					addNewSeminar(seminar);
				}

				@Override
				public void onNo() {
				}
			};
			new QuestionBox("Achtung Termin" + Umlaut.uuml + "berschneidung", e.getMessage()
					+ OverlappingSeminarsException.NEWLINE + "Termin trotzdem eintragen", yesNoHandler).show();
		} catch (IllegalArgumentException e) {
			new MessageBox("Eingabefehler", e.getMessage()).show();
		}
	}

	private void addNewSeminar(final SeminarDTO seminar) {
		this.service.addNewSeminar(seminar, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				new MessageBox("Unbekanter Fehler", "Fehler beim Festschreiben des neuen Lehrgangs: "
						+ caught.getMessage()).show();
				closeDialog(false);
			}

			@Override
			public void onSuccess(Void result) {
				new MessageBox("Aktion erfolgreich", "Der neue Lehrgang wurde erfolgreich eingetragen.").show();
				closeDialog(true);
			}
		});
	}

	protected void closeDialog(final boolean reread) {
		this.givenSeminar = null;
		hide();
		if (reread) {
			this.app.readAllSeminars();
		}
	}

	protected SeminarDTO readSeminarFromUI(final boolean validate) {
		SeminarDTO seminar = null;
		if (validate) {
			checkEmpty(tbFirstDay, tbLastDay, tbTitle, tbInstructor, tbOrganizer, tbLocation, tbAnnouncement);
			checkDates();
			checkForOverlaps();
		}
		if (givenSeminar == null) {
			seminar = new SeminarDTO();
		} else {
			seminar = new SeminarDTO(givenSeminar);
		}
		seminar.setFirstDay(TimeLogic.parse(tbFirstDay.getText()));
		seminar.setLastDay(TimeLogic.parse(tbLastDay.getText()));
		seminar.setTitle(tbTitle.getText());
		seminar.setInstructor(tbInstructor.getText());
		seminar.setOrganizer(tbOrganizer.getText());
		seminar.setLocation(tbLocation.getText());
		seminar.setAnnouncement(tbAnnouncement.getText());
		return seminar;
	}

	protected void checkForOverlaps() {
		final Seminar seminar = new SeminarDTO();
		if (this.givenSeminar != null) {
			seminar.setKey(this.givenSeminar.getKey());
		}
		seminar.setFirstDay(TimeLogic.parse(this.tbFirstDay.getText()));
		seminar.setLastDay(TimeLogic.parse(this.tbLastDay.getText()));
		final List<Seminar> overlappingSeminars = this.app.retrieveOverlappingSeminars(seminar);
		if (overlappingSeminars.size() > 0) {
			throw new OverlappingSeminarsException(overlappingSeminars);
		}
	}

	private void checkDates() {
		checkDate(this.tbFirstDay);
		checkDate(this.tbLastDay);
		final Date firstDay = TimeLogic.parse(this.tbFirstDay.getText());
		final Date lastDay = TimeLogic.parse(this.tbLastDay.getText());
		if (lastDay.getTime() < firstDay.getTime()) {
			throw new IllegalArgumentException("Bis-Datum vor Von-Datum");
		}
	}

	private void checkDate(final TextBox tb) {
		Date currentDate = null;
		try {
			currentDate = TimeLogic.parse(tb.getText());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Ung" + Umlaut.uuml + "ltige Zeichenkette \"" + tb.getText() + "\" im "
					+ tb.getTitle() + "-Datum-Feld");
		}
		if (currentDate.getTime() < (new Date()).getTime() - (3 * DAY_IN_MILLIS)) {
			throw new IllegalArgumentException("Datum im " + tb.getTitle() + "-Datum-Feld liegt in der Vergangenheit");
		}
	}

	private void checkEmpty(final TextBox... tbs) {
		for (final TextBox tb : tbs) {
			if (tb.getText().trim().length() == 0) {
				throw new IllegalArgumentException(tb.getTitle() + "-Feld leer");
			}
		}
	}
}
