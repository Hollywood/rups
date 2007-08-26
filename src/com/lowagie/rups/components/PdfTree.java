/*
 * $Id: PdfDocument.java 2884 2007-08-15 09:28:41Z blowagie $
 * Copyright (c) 2007 Bruno Lowagie
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.lowagie.rups.components;

import java.io.File;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.lowagie.rups.components.models.PdfTreeCellRenderer;
import com.lowagie.rups.factories.TreeNodeFactory;
import com.lowagie.rups.interfaces.PdfTreeNodeSelector;
import com.lowagie.rups.nodetypes.PdfObjectTreeNode;
import com.lowagie.rups.nodetypes.PdfPagesTreeNode;
import com.lowagie.rups.nodetypes.PdfTrailerTreeNode;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;

/**
 * A JTree that shows the object hierarchy of a PDF document.
 */
public class PdfTree extends JTree implements PdfTreeNodeSelector {

	/** a serial version UID */
	private static final long serialVersionUID = 7545804447512085734L;
	
	/** The root of the PDF tree. */
	protected PdfTrailerTreeNode root = new PdfTrailerTreeNode();
	
	/** The root of the page tree. */
	protected PdfPagesTreeNode pages;
	/** The root of the Outline tree. */
	protected PdfObjectTreeNode outlines;
	/** The root of the Outline tree. */
	protected PdfObjectTreeNode form;
	
	/**
	 * Constructs a PDF tree.
	 */
	public PdfTree() {
		super();
		setCellRenderer(new PdfTreeCellRenderer());
		setModel(new DefaultTreeModel(root));
	}
	
	/**
	 * Sets or resets the user object of the root.
	 * @param	file	a new file to be shown in the tree
	 */
	public void resetRoot(File file) {
		root = new PdfTrailerTreeNode();
		root.setUserObject(file);
		setModel(new DefaultTreeModel(root));
		repaint();
	}
	
	/**
	 * Sets or resets the root of this PDF tree.
	 * @param	factory	a new tree node factory for the tree
	 * @param	trailer	the trailer of a new PDF
	 */
	public void resetRoot(TreeNodeFactory factory, PdfDictionary trailer) {
		root.setTrailer(trailer);
		factory.expandNode(root);
		setModel(new DefaultTreeModel(root));
		PdfObjectTreeNode catalog = factory.getChildNode(root, PdfName.ROOT);
		pages = (PdfPagesTreeNode)factory.getChildNode(catalog, PdfName.PAGES);
		outlines = factory.getChildNode(catalog, PdfName.OUTLINES);
		form = factory.getChildNode(catalog, PdfName.ACROFORM);
		repaint();
	}

	/**
	 * Select a specific node in the tree.
	 * Typically this method will be called from a different tree,
	 * such as the pages, outlines or form tree.
	 * @param	node	the node that has to be selected
	 */
	public void selectNode(PdfObjectTreeNode node) {
		TreePath path = new TreePath(node.getPath());
		setSelectionPath(path);
		scrollPathToVisible(path);
	}

	/**
	 * Gets the root of the page tree
	 * @return	the top level PdfPagesTreeNode
	 */
	public PdfPagesTreeNode getPages() {
		return pages;
	}

	/**
	 * Gets the root of the outline tree.
	 * @return	the top level Outline TreeNode
	 */
	public PdfObjectTreeNode getOutlines() {
		return outlines;
	}

	/**
	 * Gets the root of the form tree.
	 * @return	the top level Outline TreeNode
	 */
	public PdfObjectTreeNode getForm() {
		return form;
	}
}