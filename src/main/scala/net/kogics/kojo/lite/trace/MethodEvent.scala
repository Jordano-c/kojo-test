/*
 * Copyright (C) 2013 "Sami Jaber" <jabersami@gmail.com>
 * Copyright (C) 2013 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */
package net.kogics.kojo.lite.trace

import com.sun.jdi.LocalVariable
import com.sun.jdi.StackFrame
import java.awt.geom.Point2D

class MethodEvent {
  var methodName: String = _
  var args: Seq[String] = _
  var returnVal: String = _
  var parent: Option[MethodEvent] = None
  var entryLineNum: Int = _
  var exitLineNum: Int = _
  var sourceName: String = _
  var callerLineNum: Int = -1
  var callerSourceName: String = _
  var callerLine: String = _
  var subcalls = Vector[MethodEvent]()
  var turtlePoints: Option[(Point2D.Double, Point2D.Double)] = None

  def pargs = args.mkString("(", ", ", ")")
  def pret = returnVal.replaceAllLiterally("<void value>", "Unit")
  def psubcalls = _psubcalls(subcalls)
  def _psubcalls(scs: Seq[MethodEvent]): String = scs match {
    case Seq() => ""
    case Seq(x, xs @ _*) => s"> ${x.methodName} ${_psubcalls(x.subcalls)} < ${_psubcalls(xs)}"
  }


  def entry(level: Int) = {
    <html><div style="font-family:Monospace"><span style="color:rgb(0,50,225)">{ "> " * level } [Enter]</span> { methodName }<span style="color:rgb(0,50,225)">{ pargs }</span></div></html>.toString
  }

  def exit(level: Int) = {
    <html><div style="font-family:Monospace"><span style="color:rgb(225,50,0)">{ "< " * level } [Exit]</span> { methodName } <span style="color:rgb(225,50,0)">[{ pret }]</span></div></html>.toString
  }

  override def toString() = {
    s"""Name: $methodName
Args: $pargs ${if (returnVal != null) s"\nReturn Value: $returnVal" else ""}
Source: $sourceName
Entry Line Number: $entryLineNum
Exit Line Number: $exitLineNum
Caller Source: $callerSourceName
Caller Line Number: $callerLineNum
Caller Source Line: $callerLine
"""
  }

  def ended = returnVal != null

  def setParent(p: Option[MethodEvent]) {
    parent = p
    parent foreach { _.addChild(this) }
  }

  private def addChild(c: MethodEvent) { subcalls = subcalls :+ c }

  def level: Int = parent match {
    case None    => 0
    case Some(p) => p.level + 1
  }
}