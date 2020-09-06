package jdraw.std.commands;

import java.awt.Point;

import jdraw.framework.DrawCommand;
import jdraw.framework.Figure;

/**
 * Implements a command for the set bounds operation on a figure.
 * @author Dominik Gruntz
 */
public class SetBoundsCommand implements DrawCommand {
	private static final long serialVersionUID = -8144149584989831877L;

	/** The figure on which set bounds was applied. */
	private final Figure figure;
	/** The coordinates of the original origin. */
	private final Point fromOrig;
	/** The coordinates of the original corner. */
	private final Point fromCorn;
	/** The coordinates of the changed origin. */
	private final Point toOrig;
	/** The coordinates of the changed corner. */
	private final Point toCorn;

	/**
	 * An set bounds command contains the information about the 
	 * figure whose bounds are changed and the origin and the corner
	 * of the original and the changed figure.
	 * @param figure the figure on which the set bounds command was applied.
	 * @param fromOrig the original origin of the figure.
	 * @param fromCorn the original corner of the figure.
	 * @param toOrig the changed origin of the figure.
	 * @param toCorn the changed corner of the figure.
	 */
	public SetBoundsCommand(Figure figure, Point fromOrig, Point fromCorn, Point toOrig, Point toCorn) {
		this.figure = figure;
		this.fromOrig = fromOrig;
		this.fromCorn = fromCorn;
		this.toOrig = toOrig;
		this.toCorn = toCorn;
	}

	/**
	 * Apply the stored set bounds operation again.
	 */
	@Override
	public void redo() {
		figure.setBounds(toOrig, toCorn);
	}

	/** 
	 * Undo the stored set bounds operation, i.e. set the ofiginal bounds of the figure.
	 */
	@Override
	public void undo() {
		figure.setBounds(fromOrig, fromCorn);
	}

}

