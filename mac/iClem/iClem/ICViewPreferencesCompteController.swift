//
//  ICViewPreferencesCompteController.swift
//  iClem
//
//  Created by Odric Roux-Paris on 08/03/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

import Cocoa
import Clem

enum StatePreferencesComptes{ //Pour savoir où en est le refresh
    case Refresh
    case NotRefresh
}
enum StatePreferenceShow{
    case FirstShow
    case NotFirstShow
}
class ICViewPreferencesCompteController: NSViewController {

    @IBOutlet weak var biographie: NSTextField!
    @IBOutlet weak var portrait: NSImageView!
    @IBOutlet weak var username: NSTextField!
    @IBOutlet weak var urlSiteInternet: NSTextField!
    @IBOutlet weak var signature: NSTextField!
    @IBOutlet weak var progressIndicator: NSProgressIndicator!
    
    var loadingController: ICLoadingViewController!
    
    
    var timerRefresh:NSTimer!;
    var stateRefresh = StatePreferencesComptes.NotRefresh
    
    var stateShow = StatePreferenceShow.FirstShow
    
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
        /*
        profile.saveProfil(membre, asBloc: { (success, error) -> Void in
            self.progressIndicator.stopAnimation(nil)
            self.progressIndicator.hidden = true
        })
*/
    }

    @IBAction func openSite(sender: AnyObject) {
        if urlSiteInternet.stringValue != ""{
            NSWorkspace.sharedWorkspace().openURL(NSURL(string: urlSiteInternet.stringValue)!)
        }
    }
    
    override func viewDidAppear() {
        checkParameterProfile()
        timerRefresh = NSTimer.scheduledTimerWithTimeInterval(10,
            target: self,
            selector:Selector("checkParameterProfile")
            , userInfo: nil,
            repeats: true)
    }
    
    func checkParameterProfile() {
        /*
        Fonction qui télécharge les paramètres et met à jour à l'interface
        */
        if stateRefresh == .Refresh{ //Si il fait déjà un rafraichissement
            return
        }
        progressIndicator.startAnimation(nil)
        //Téléchargement des données
        stateRefresh = .Refresh
        self.showLoadingView()
        profile.checkParametreProfil { (membre, e) -> Void in
            if ((e) != nil){
                return
            }
            self.progressIndicator!.stopAnimation(nil)
            self.progressIndicator.hidden = true
            self.updateUserWithMembre(membre)
            self.membre = membre
            self.stateRefresh = .NotRefresh
            
            self.hideLoadingView()
            
        
        }

    }
    
    func showLoadingView() {
        //LoadingView
        if stateShow == .NotFirstShow{
            return
        }
        let story = NSStoryboard(name: "Main", bundle: nil)
        loadingController = story?.instantiateControllerWithIdentifier("LoadingView") as ICLoadingViewController
        
        self.presentViewController(loadingController, animator: ICOpacitySegue())
        
        if stateShow == .FirstShow{
            stateShow = .NotFirstShow
        }
    }
    func hideLoadingView(){
        loadingController.dismissController(nil)
    }
    func updateUserWithMembre(membre:CMembre){
        biographie.stringValue = membre.biography
        username.stringValue = membre.username
        urlSiteInternet.stringValue = membre.site.absoluteString!
        signature.stringValue = membre.sign
        portrait.image = NSImage(contentsOfURL: membre.urlAvatar)
        portrait.layer?.cornerRadius = 10
        
        
        println(biographie.stringValue)
        
        
        //On rechange l'image
        //portrait.bounds.size = membre.avatar.size
        //portrait.frame.origin.x
        
    }
    
}
