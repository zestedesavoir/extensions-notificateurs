//
//  ICLoginController.swift
//  iClem
//
//  Created by Odric Roux-Paris on 17/01/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

import Cocoa
import Clem
import QuartzCore
class ICLoginController: NSViewController {

    @IBOutlet weak var progress: NSProgressIndicator!
    @IBOutlet weak var password: NSSecureTextField!

    @IBOutlet weak var login: NSTextField!
    @IBOutlet weak var imageClem: NSImageView!
    @IBOutlet weak var buttonConnect: ICButtonConnect!
    
    //Variable pour les animations
    var layerError:CALayer!
    var layerTextError:CATextLayer!
    
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do view setup here.
        progress.hidden = true
        
        
        
        self.view.layer = CALayer()
        self.view.layer?.backgroundColor = CGColorCreateGenericRGB(0, 0.27, 0.38, 1);
        
        layerError = CALayer();
        layerError.position = CGPoint(x: 0, y:(password.frame.origin.y-password.frame.size.height)-20 )
        layerError.bounds = NSRect(x: 0, y: 0, width: 0, height: 25)
        layerError.anchorPoint = CGPointZero
        layerError.backgroundColor = CGColorCreateGenericRGB(0.82, 0.09, 0.09, 1)
        //  layerTextError.position = CGPoint(x: 0,y: 0)
        
        buttonConnect.enabled(false)
        
        
        if let isNil = self.view.layer{
            self.view.layer?.addSublayer(layerError)
        }
        
        println(self.view.layer?.sublayers)
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: Selector("textDidChange:"), name: NSControlTextDidChangeNotification, object: nil)
        
        
        
    }
    @IBAction func actionLogin(sender: AnyObject) {
        
        var connect = CConnectLogin(username: login.stringValue, password: password.stringValue, clientID:"A35VQbMSXQtLX4FJJK9vqHb5COYabF6ntpi.zt4h" , clientScrete:"u:2@SgFgGvyqoEUJW-WylMUkhcHpQtfs_QNeExNTJ.RzknY6;Jis5Rc?2ob9N-HRZ:?X2lKxvF9Y1EmHlor8OwyWems;xdObycxSLJ5N;60nJM7jhX6C7v0!UMjfLF:x" )
        progress.hidden = false
        progress.startAnimation(nil)
        buttonConnect.charge(true)
        connect.connectionZDSWithBloc { (re, a, error) -> Void in
            if let errorValue = error{
                let clemPasContent = NSImage(named: "clem_pas_contente")
                
                self.imageClem.image = clemPasContent

                if errorValue.domain == "Vous n'avez pas bien saisi votre mot de passe ou votre login"{
                    self.animateNoLogin(NSError(domain: "Mot de passe incorrect", code: 500, userInfo: nil))
                    }else{
                
                self.animateNoLogin(errorValue)
                }
            }else{
                
                                
                self.view.window?.close()
            }
            self.progress.hidden = true
            self.progress.stopAnimation(nil)
        }
    }
    
    func animateNoLogin(error: NSError){
        //Préparation du texte
        /*
        let stringErreur = error.domain
        let font:NSFont = NSFont.systemFontOfSize(layerTextError.fontSize)
        let attribute = [NSFontAttributeName:font]
        let size = stringErreur.sizeWithAttributes(attribute)
        let rectView = self.view.bounds

        
        
        
        let rectCenter = NSMakeRect((rectView.size.width/2)-(size.width/2), 85, size.width+5, size.height+5)
        let rectText = NSMakeRect(0, 0, rectCenter.size.width, rectCenter.size.height)
        

        
        let animationErrorLayer = CABasicAnimation(keyPath: "size.width")
        animationErrorLayer.duration = 0.5
        animationErrorLayer.timingFunction = CAMediaTimingFunction(name: kCAMediaTimingFunctionEaseInEaseOut)
        animationErrorLayer.toValue = NSValue(size: NSSize(width: 0, height: 52))
        animationErrorLayer.fromValue = NSValue(size: NSSize(width: self.view.bounds.size.width, height: 52))
        
        
       */
        
    
        let animationKeyFrame = CAKeyframeAnimation()
        animationKeyFrame.keyPath = "position.x"
        animationKeyFrame.values = [0, 10, -10, 10, 0]
        animationKeyFrame.keyTimes = [0, (1/6.0), (3/6.0), (5/6.0), 1]
        animationKeyFrame.duration = 0.3
        animationKeyFrame.additive = true
        CATransaction.begin()
        login.layer?.addAnimation(animationKeyFrame, forKey: "shake")
        password.layer?.addAnimation(animationKeyFrame, forKey: "shake")
        /*
        if layerError.frame != rectCenter{
        layerError.addAnimation(animationErrorLayer, forKey: "error")
        }
*/
        CATransaction.commit()
        

               


        
    }
    
    override func viewDidDisappear() {
        password.editable = false
        login.editable = false
    }
    override func viewDidAppear() {
        password.editable = true
        login.editable = true
    }
    
    func textDidChange(notification: NSNotification){
        let userInfos = notification.userInfo as Dictionary!
        if (password.stringValue != "" && login.stringValue != ""){
            buttonConnect.enabled(true)
        }else{
             buttonConnect.enabled(false)
        }
    }
}
