//
//  ICTextFieldConnection.swift
//  iClem
//
//  Created by Odric Roux-Paris on 18/02/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

import Cocoa

class ICTextFieldConnection: NSTextField {
    
    var bezierPath:NSBezierPath!
    var error:Bool = false {
        didSet{
            setNeedsDisplay()
            
            //Si une error, on efface le text
          
        }
        
    }
    override func awakeFromNib() {
        self.layer?.cornerRadius = 0.0
        var bounds = self.bounds
        
        
        bounds.origin.x += 1
        bounds.origin.y += 1

        self.bezierPath = NSBezierPath(rect: bounds)
        self.bezierPath.lineWidth = 2.0

        
     }
    override func drawRect(dirtyRect: NSRect) {
        super.drawRect(dirtyRect)
        NSColor(calibratedRed:0.02, green:0.27, blue:0.39, alpha:1).set()
        bezierPath.stroke()
    }
    
    }
    
   
    
