package org.codehaus.griffon.jsilhouette.geom

import groovy.text.SimpleTemplateEngine

def basedir = args[0]
def apidocs =  new File(basedir, 'build/docs/javadoc')
def javadocs = new File(basedir, 'build/classes/examples')
def basepckg = 'org.codehaus.griffon.jsilhouette.geom'

def templateEngine = new SimpleTemplateEngine()
def template = '''

import java.awt.Robot
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import ${package_name}.${class_name}

def frame = ${class_name}.buildUI()
/*
frame.visible = true
Thread.sleep(500)
ImageIO.write(
  new Robot().createScreenCapture(frame.bounds),
  "png",
  targetFile
)
frame.visible = false
*/
frame.visible = true
def canvas = frame.contentPane.components[0]
def image = new BufferedImage(
   canvas.size.width as int,
   canvas.size.height as int,
   BufferedImage.TYPE_INT_RGB
)
def g = image.createGraphics()
canvas.paint(g)
g.dispose()
ImageIO.write(
  image,
  "png",
  targetFile
)
frame.visible = false
'''

def shapes = []
def examplesDir = new File(javadocs,basepckg.replace('.',File.separator))
def apidocsDir = new File(apidocs,basepckg.replace('.',File.separator))

examplesDir.eachFileMatch({it.endsWith("Example.class")}) { shapeFile ->
   def shape = (shapeFile.name - "Example.class")
   shapes << shape
   def binding = [
     package_name: basepckg,
     class_name: shapeFile.name - ".class"
   ]
   def script = templateEngine.createTemplate(template).make(binding)
   binding = new Binding()
   binding.setVariable("targetFile", new File(apidocsDir,"${shape}.png"))
   new GroovyShell(getClass().classLoader,binding).evaluate(script.toString())
}

apidocsDir.eachFileMatch({(it - ".html") in shapes}) { shapeFile ->
   template = shapeFile.text
   def java2html = []
   new File(examplesDir, (shapeFile.name - ".html" + "Example.java.html")).eachLine{ line -> java2html << line }
   def code = java2html[14..-6].join("")
   def binding = [
      img: """<img src="${shapeFile.name - '.html'}.png"/>""",
      code: code
   ]
   shapeFile.text = templateEngine.createTemplate(template).make(binding).toString()
}
