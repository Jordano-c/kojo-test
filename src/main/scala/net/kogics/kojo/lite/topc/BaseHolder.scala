package net.kogics.kojo.lite.topc

import java.awt.Color
import javax.swing.JComponent

import bibliothek.gui.dock.common.mode.ExtendedMode
import bibliothek.gui.dock.common.CLocation
import bibliothek.gui.dock.common.DefaultSingleCDockable

class BaseHolder(id: String, title: String, component: JComponent)
    extends DefaultSingleCDockable(id, title, component) {
  if (component != null) {
    component.setBackground(Color.white)
  }
  setDefaultLocation(ExtendedMode.MINIMIZED, CLocation.base.minimalWest)
}
