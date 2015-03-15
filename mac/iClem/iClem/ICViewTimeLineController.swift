//
//  ICViewTimeLineController.swift
//  iClem
//
//  Created by Odric Roux-Paris on 05/03/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

import Cocoa
import Clem
class ICViewTimeLineController: NSViewController {

    let profileDownload: CProfileAll = CProfileAll()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do view setup here.
        
        
        
    }
    override func viewDidAppear() {
        //on appelle le controller
        let story = NSStoryboard(name: "Main", bundle: nil)
        let controller: ICDownloadMembreController = story?.instantiateControllerWithIdentifier("DownloadView") as ICDownloadMembreController
        controller.profileDownload = profileDownload

        self.presentViewControllerAsSheet(controller)
        
            }
    
    override func awakeFromNib() {
        
        

        

    }
    
}
