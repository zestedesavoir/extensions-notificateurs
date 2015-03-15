//
//  ICInscriptionController.swift
//  iClem
//
//  Created by Odric Roux-Paris on 17/01/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

import Cocoa
import Clem
class ICInscriptionController: NSViewController {

    @IBOutlet weak var mail: NSTextField!
    @IBOutlet weak var password: NSSecureTextField!
    @IBOutlet weak var username: NSTextField!
    
    @IBOutlet weak var labelInformation: NSTextField!
      override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    @IBAction func actionInscription(sender: AnyObject) {
        let inscription = CInscription(username: username.stringValue, password: password.stringValue, EMail: mail.stringValue)
        inscription.createUserAsBloc { (user, mail, error) -> Void in
            if let e = error{
                self.animate()
                self.labelInformation.stringValue = error.domain
            }
        }
    }
    func animate(){
        let animationKeyFrame = CAKeyframeAnimation()
        animationKeyFrame.keyPath = "position.x"
        animationKeyFrame.values = [0, 10, -10, 10, 0]
        animationKeyFrame.keyTimes = [0, (1/6.0), (3/6.0), (5/6.0), 1]
        animationKeyFrame.duration = 0.3
        animationKeyFrame.additive = true

        CATransaction.begin()
        mail.layer?.addAnimation(animationKeyFrame, forKey: "shake")
        password.layer?.addAnimation(animationKeyFrame, forKey: "shake")
        username.layer?.addAnimation(animationKeyFrame, forKey: "shake")
        CATransaction.commit()
        
    }
}
