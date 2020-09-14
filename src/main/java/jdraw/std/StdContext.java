/*
 * Copyright (c) 2018 Fachhochschule Nordwestschweiz (FHNW)
 * All Rights Reserved.
 */
package jdraw.std;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import jdraw.figures.RectTool;
import jdraw.figures.OvalTool2;
import jdraw.figures.Group;
import jdraw.figures.LineTool2;
import jdraw.framework.DrawCommandHandler;
import jdraw.framework.DrawModel;
import jdraw.framework.DrawTool;
import jdraw.framework.DrawToolFactory;
import jdraw.framework.DrawView;
import jdraw.framework.Figure;

/**
 * Standard implementation of interface DrawContext.
 * 
 * @see DrawView
 * @author Dominik Gruntz &amp; Christoph Denzler
 * @version 2.6, 24.09.09
 */
@SuppressWarnings("serial")
public class StdContext extends AbstractContext {
	/**
	 * Constructs a standard context with a default set of drawing tools.
	 * @param view the view that is displaying the actual drawing.
	 */
	public StdContext(DrawView view) {
		super(view, null);
	}

	private List<Figure> sel;
	private boolean isCut =false;

	/**
	 * Constructs a standard context. The drawing tools available can be
	 * parameterized using <code>toolFactories</code>.
	 * 
	 * @param view  the view that is displaying the actual drawing.
	 * @param toolFactories  a list of DrawToolFactories that are available to the user
	 */
	public StdContext(DrawView view, List<DrawToolFactory> toolFactories) {
		super(view, toolFactories);
	}

	/**
	 * Creates and initializes the "Edit" menu.
	 * 
	 * @return the new "Edit" menu.
	 */
	@Override
	protected JMenu createEditMenu() {
		JMenu editMenu = new JMenu("Edit");
		final JMenuItem undo = new JMenuItem("Undo");
		undo.setAccelerator(KeyStroke.getKeyStroke("control Z"));
		editMenu.add(undo);
		undo.addActionListener(e -> {
				final DrawCommandHandler h = getModel().getDrawCommandHandler();
				if (h.undoPossible()) {
					h.undo();
				}
			}
		);

		final JMenuItem redo = new JMenuItem("Redo");
		redo.setAccelerator(KeyStroke.getKeyStroke("control Y"));
		editMenu.add(redo);
		redo.addActionListener(e -> {
				final DrawCommandHandler h = getModel().getDrawCommandHandler();
				if (h.redoPossible()) {
					h.redo();
				}
			}
		);
		editMenu.addSeparator();

		JMenuItem sa = new JMenuItem("SelectAll");
		sa.setAccelerator(KeyStroke.getKeyStroke("control A"));
		editMenu.add(sa);
		sa.addActionListener( e -> {
				getModel().getFigures().forEachOrdered(f -> getView().addToSelection(f));
				getView().repaint();
			}
		);



		editMenu.addSeparator();

		
		JMenuItem cut = new JMenuItem("Cut");
		cut.setAccelerator(KeyStroke.getKeyStroke("control X"));
		cut.addActionListener(e -> {
			isCut = true;
			setSel(getView().getSelection());
			getView().getSelection().forEach(a -> getModel().removeFigure(a));
			//getView().clearSelection();
		});
		editMenu.add(cut).setEnabled(true);
		JMenuItem copy = new JMenuItem("Copy");
		copy.setAccelerator(KeyStroke.getKeyStroke("control C"));
		copy.addActionListener(e -> {setSel(getView().getSelection());});
		editMenu.add(copy).setEnabled(true);
		JMenuItem paste = new JMenuItem("Paste");
		paste.setAccelerator(KeyStroke.getKeyStroke("control V"));
		paste.addActionListener(e -> {
			int dx =0,dy=0; 
			//Idee wäre, dass wenn Maus ausserhalb Frame, dann kopiert sie es an stelle
			//if (getBounds().contains(getMousePosition()) ){
				
				dx = getMousePosition().x-sel.get(0).getBounds().x;
				dy = getMousePosition().y-sel.get(0).getBounds().y;
			//}

			if (isCut) {
				for (Figure f : sel) {
					f.move(dx, dy);
					getModel().addFigure(f);
					getView().addToSelection(f);
				}
				isCut = false;
			}
			else {
				for (Figure f : sel) {
					Figure g = f.clone();
					g.move(dx, dy);
					getModel().addFigure(g);
					getView().addToSelection(g);
				}
			}

			

		});
		editMenu.add(paste).setEnabled(true);

		editMenu.addSeparator();
		JMenuItem clear = new JMenuItem("Clear");
		editMenu.add(clear);
		clear.addActionListener(e -> {
			getModel().removeAllFigures();
		});
		
		editMenu.addSeparator();
		JMenuItem group = new JMenuItem("Group");
		group.setEnabled(true);
		group.addActionListener(e -> {
			getModel().addFigure(new Group(getView().getSelection()));
			for (Figure f : getView().getSelection()) {
				getModel().removeFigure(f);
			}
			});
		editMenu.add(group);

		JMenuItem ungroup = new JMenuItem("Ungroup");
		ungroup.setEnabled(true);
		ungroup.addActionListener(e -> {
			List<Figure> list = getView().getSelection();
			for (Figure f : list) {
				if (f instanceof Group) {
					Group ff = (Group) f;
					ff.getFigureParts().forEach(x->getModel().addFigure(x));
					getModel().removeFigure(f);
				}

			}
			});
		editMenu.add(ungroup);

		editMenu.addSeparator();

		JMenu orderMenu = new JMenu("Order...");
		JMenuItem frontItem = new JMenuItem("Bring To Front");
		frontItem.addActionListener(e -> {
			bringToFront(getView().getModel(), getView().getSelection());
		});
		orderMenu.add(frontItem);
		JMenuItem backItem = new JMenuItem("Send To Back");
		backItem.addActionListener(e -> {
			sendToBack(getView().getModel(), getView().getSelection());
		});
		orderMenu.add(backItem);
		editMenu.add(orderMenu);

		JMenu grid = new JMenu("Grid...");
		grid.add("Grid 1");
		grid.add("Grid 2");
		grid.add("Grid 3");
		editMenu.add(grid);
		
		return editMenu;
	}

	/**
	 * Creates and initializes items in the file menu.
	 * 
	 * @return the new "File" menu.
	 */
	@Override
	protected JMenu createFileMenu() {
		JMenu fileMenu = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		fileMenu.add(open);
		open.setAccelerator(KeyStroke.getKeyStroke("control O"));
		open.addActionListener(e -> doOpen());

		JMenuItem save = new JMenuItem("Save");
		save.setAccelerator(KeyStroke.getKeyStroke("control S"));
		fileMenu.add(save);
		save.addActionListener(e ->	doSave());

		JMenuItem exit = new JMenuItem("Exit");
		fileMenu.add(exit);
		exit.addActionListener(e -> System.exit(0));
		
		return fileMenu;
	}

	@Override
	protected void doRegisterDrawTools() {
		// TOD Add new figure tools here
		DrawTool rectangleTool = new RectTool(this);
		addTool(rectangleTool);
		DrawTool ovalTool = new OvalTool2(this);
		addTool(ovalTool);
		DrawTool lineTool = new LineTool2(this);
		addTool(lineTool);
	}

	/**
	 * Changes the order of figures and moves the figures in the selection
	 * to the front, i.e. moves them to the end of the list of figures.
	 * @param model model in which the order has to be changed
	 * @param selection selection which is moved to front
	 */
	public void bringToFront(DrawModel model, List<Figure> selection) {
		// the figures in the selection are ordered according to the order in the model
		List<Figure> orderedSelection = model.getFigures().filter(f -> selection.contains(f)).collect(Collectors.toList());
		Collections.reverse(orderedSelection);
		int pos = (int) model.getFigures().count();
		for (Figure f : orderedSelection) {
			model.setFigureIndex(f, --pos);
		}
	}

	/**
	 * Changes the order of figures and moves the figures in the selection
	 * to the back, i.e. moves them to the front of the list of figures.
	 * @param model model in which the order has to be changed
	 * @param selection selection which is moved to the back
	 */
	public void sendToBack(DrawModel model, List<Figure> selection) {
		// the figures in the selection are ordered according to the order in the model
		List<Figure> orderedSelection = model.getFigures().filter(f -> selection.contains(f)).collect(Collectors.toList());
		int pos = 0;
		for (Figure f : orderedSelection) {
			model.setFigureIndex(f, pos++);
		}
	}

	/**
	 * Handles the saving of a drawing to a file.
	 */
	private void doSave() {
		JFileChooser chooser = new JFileChooser(getClass().getResource("").getFile());
		chooser.setDialogTitle("Save Graphic");
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		
		chooser.setFileFilter(new FileNameExtensionFilter("JDraw Graphics (*.draw)", "draw"));
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("JDraw Graphics (*.xml)", "xml"));
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("JDraw Graphics (*.json)", "json"));
		
		int res = chooser.showSaveDialog(this);

		if (res == JFileChooser.APPROVE_OPTION) {
			// save graphic
			File file = chooser.getSelectedFile();
			FileFilter filter = chooser.getFileFilter();
			if(filter instanceof FileNameExtensionFilter && !filter.accept(file)) {
				file = new File(chooser.getCurrentDirectory(), file.getName() + "." + ((FileNameExtensionFilter)filter).getExtensions()[0]);
			}
			System.out.println("save current graphic to file " + file.getName() + " using format "
					+ ((FileNameExtensionFilter)filter).getExtensions()[0]);
		}
	}

	/**
	 * Handles the opening of a new drawing from a file.
	 */
	private void doOpen() {
		JFileChooser chooser = new JFileChooser(getClass().getResource("")
				.getFile());
		chooser.setDialogTitle("Open Graphic");
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			@Override
			public String getDescription() {
				return "JDraw Graphic (*.draw)";
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().endsWith(".draw");
			}
		});
		int res = chooser.showOpenDialog(this);

		if (res == JFileChooser.APPROVE_OPTION) {
			// read jdraw graphic
			System.out.println("read file "
					+ chooser.getSelectedFile().getName());
		}
	}

	public List<Figure> getSel() {
		return sel;
	}

	public void setSel(List<Figure> sel) {
		this.sel = sel;
	}

}
