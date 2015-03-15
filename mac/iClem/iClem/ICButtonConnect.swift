//
//  ICButton.swift
//  iClem
//
//  Created by Odric Roux-Paris on 13/02/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

import Cocoa

class ICButtonConnect: NSButton {

    override var wantsUpdateLayer:Bool {return true}
    let layerGreyEnabled:CALayer = CALayer()

    
    override func awakeFromNib() {
        self.layer = CALayer()
        self.layer?.cornerRadius = 2
        self.layer?.backgroundColor = CGColorCreateGenericRGB(0, 0.67, 0.02, 1)
        
        
       
        let textColor = NSColor.whiteColor()
        let colortitle = NSMutableAttributedString(attributedString: self.attributedTitle)
        let titleRange = NSMakeRange(0, colortitle.length)
        colortitle.addAttribute(NSForegroundColorAttributeName, value: textColor, range: titleRange)
        self.attributedTitle = colortitle
        
        
        //On track la souris 
        let trackingArea = NSTrackingArea(rect: self.bounds, options: NSTrackingAreaOptions.ActiveAlways | NSTrackingAreaOptions.MouseEnteredAndExited, owner: self, userInfo: nil)
        self.addTrackingArea(trackingArea)
        
        
        layerGreyEnabled.backgroundColor = CGColorCreateGenericRGB(0.66, 0.66, 0.66, 1)
        layerGreyEnabled.anchorPoint = CGPointZero
        layerGreyEnabled.bounds = self.bounds
        layerGreyEnabled.cornerRadius = 3
        self.layer?.addSublayer(layerGreyEnabled)
        
}
    
    override func drawRect(dirtyRect: NSRect) {
        super.drawRect(dirtyRect)

        // Drawing code here.
      
    }
    
    override func updateLayer() {
        super.updateLayer()
    }
    
    override func mouseEntered(theEvent: NSEvent) {
        self.layer?.backgroundColor = CGColorCreateGenericRGB(0.01, 0.82, 0.03, 1)
    }
    override func mouseExited(theEvent: NSEvent) {
        self.layer?.backgroundColor = CGColorCreateGenericRGB(0, 0.67, 0.02, 1)
    }
    
    func enabled (enabled: Bool) {
        self.enabled = enabled
        let animation = CABasicAnimation(keyPath: "size.width")
        animation.timingFunction = CAMediaTimingFunction(name: kCAMediaTimingFunctionEaseInEaseOut)

        
        if (enabled && layerGreyEnabled.bounds.size.width != 0){
            animation.fromValue = NSValue(rect: layerGreyEnabled.bounds)
            layerGreyEnabled.addAnimation(animation, forKey: "animationEnabled")
            layerGreyEnabled.bounds.size = CGSize(width: 0, height: layerGreyEnabled.bounds.size.height)
            
        }else  if !enabled && layerGreyEnabled.bounds.size.width == 0{
            animation.fromValue = NSValue(size: CGSize(width: 0, height: layerGreyEnabled.bounds.size.height))
            layerGreyEnabled.addAnimation(animation, forKey: "animationEnabled")
            layerGreyEnabled.bounds.size = CGSize(width: self.bounds.size.width, height: layerGreyEnabled.bounds.size.height)
        }
        
      
    }
    
    func charge(finish:Bool){
       // self.enabled(!finish)
        if finish{
            let animation = CAKeyframeAnimation(keyPath: "size.width")
            var size = self.bounds.size
            animation.values = [size.width,size.width/2,size.width]
            animation.keyTimes = [1]
            animation.timingFunction = CAMediaTimingFunction(name: kCAMediaTimingFunctionEaseInEaseOut)
            animation.repeatCount = -1
            
            layer?.addAnimation(animation, forKey: "Chargement")
            
        }
    }
    
    
    
    
}
