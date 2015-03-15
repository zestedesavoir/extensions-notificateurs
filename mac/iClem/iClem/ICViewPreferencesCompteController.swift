//
//  ICViewPreferencesCompteController.swift
//  iClem
//
//  Created by Odric Roux-Paris on 08/03/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

import Cocoa
import Clem
class ICViewPreferencesCompteController: NSViewController {

    @IBOutlet weak var biographie: NSTextField!
    @IBOutlet weak var portrait: NSImageView!
    @IBOutlet weak var username: NSTextField!
    @IBOutlet weak var urlSiteInternet: NSTextField!
    @IBOutlet weak var signature: NSTextField!
    @IBOutlet weak var progressIndicator: NSProgressIndicator!
    
    let profile = CProfile()
    var membre:CMembre!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
      
    }
    @IBAction func saveProfile(sender: AnyObject) {
        membre.biography = biographie.stringValue

        membre.site = NSURL(string: urlSiteInternet.stringValue)
        membre.sign = signature.stringValue

        
        
        
        progressIndicator.hidden = false
        progressIndicator.startAnimation(nil)
        profile.saveProfil(membre, asBloc: { (success, error) -> Void in
            self.progressIndicator.stopAnimation(nil)
            self.progressIndicator.hidden = true
        })
    }

    override func viewDidAppear() {
        progressIndicator.startAnimation(nil)
        //Téléchargement des données
        profile.checkParametreProfil { (membre, e) -> Void in
            self.progressIndicator!.stopAnimation(nil)
            self.progressIndicator.hidden = true
            self.updateUserWithMembre(membre)
            self.membre = membre
            
        }
    }
    override func awakeFromNib() {
       
    }
    func updateUserWithMembre(membre:CMembre){
        biographie.stringValue = membre.biography
        portrait.image = membre.avatar
        username.stringValue = membre.username
        urlSiteInternet.stringValue = membre.site.absoluteString!
        signature.stringValue = membre.sign
        
        
        //On rechange l'image
        //portrait.bounds.size = membre.avatar.size
        //portrait.frame.origin.x
        
    }
    
}
