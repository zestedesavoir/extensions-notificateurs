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


enum SateLogin{
    case Connexion
    case NotConnect
}

class ICLoginController: NSViewController {

    @IBOutlet weak var progress: NSProgressIndicator!
    @IBOutlet weak var password: NSSecureTextField!

    @IBOutlet weak var login: NSTextField!
    @IBOutlet weak var imageClem: NSImageView!
    @IBOutlet weak var buttonConnect: ICButtonConnect!
    
    var state = SateLogin.NotConnect
    
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
        self.connect()
    }
    
    func connectWithSSKeychain() {
        if let log = SSKeychain.accountsForService("iClem") {
            
        login.stringValue = log[0]["acct"] as String
        password.stringValue = SSKeychain.passwordForService("iClem", account: login.stringValue)
            
            self.connect()
        }
        
    }
    func connect (){
        /*
        let token = CToken(acessToken: "0bxnlha9Bm1A7igcptvOInLi7merp1", refreshToken:"S5PzIB0hkuz1caPXEaSEtv7Hy4z1xe" , expireInDate: nil)
        CToken.setDefaultToken(token)

        */
        
        var connect = CConnectLogin(username: login.stringValue,
            password: password.stringValue,
            clientID:"A35VQbMSXQtLX4FJJK9vqHb5COYabF6ntpi.zt4h" ,
            clientScrete:"u:2@SgFgGvyqoEUJW-WylMUkhcHpQtfs_QNeExNTJ.RzknY6;Jis5Rc?2ob9N-HRZ:?X2lKxvF9Y1EmHlor8OwyWems;xdObycxSLJ5N;60nJM7jhX6C7v0!UMjfLF:x" )
        
        progress.hidden = false
        progress.startAnimation(nil)
        password.enabled = false
        login.enabled = false
        
        connect.connectionZDSWithBloc { (t, error) -> Void in
            
            self.password.enabled = true
            self.login.enabled = true
            
            
            //Erreur
            if let errorValue = error{
                let clemPasContent = NSImage(named: "clem_pas_contente")
                
                self.imageClem.image = clemPasContent
                
                if errorValue.domain == "Vous n'avez pas bien saisi votre mot de passe ou votre login"{
                    self.animateError()
                }else{
                    
                    self.animateError()
                }
                //Success
            }else{
                println(t.accessToken)
                println(t.refreshToken)

                var hasUseKeychain:Bool = false
                               for info in SSKeychain.accountsForService("iClem"){
                    if let s = info["acct"] as? String{
                        if s == self.login.stringValue{
                            hasUseKeychain = true
                        }
                    }
                }
                if boolConnectDefault() && !hasUseKeychain{//&& SSKeychain.passwordForService("iClem", account: self.login.stringValue){
                //On met le password dans SSKEychain, s'il accèpte
                    SSKeychain.setPassword(self.password.stringValue, forService: "iClem", account: self.login.stringValue)
                    println("Il sont passé là")
                }
                
                self.view.window?.close()
            }
            self.progress.hidden = true
            self.progress.stopAnimation(nil)
        }
        

        
    }

    
    
    func animateError(){
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
    
    
    @IBAction func connectDefaultAction(sender: NSButton) {
        setBoolConnectDefault(Bool(sender.state))
    }

    override func viewDidDisappear() {
        password.editable = false
        login.editable = false
    }
    override func viewDidAppear() {
        password.editable = true
        login.editable = true
        
        /*
        Connection par défault
        */
        if boolConnectDefault(){
            self.connectWithSSKeychain()
        }

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
