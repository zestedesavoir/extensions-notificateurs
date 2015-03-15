//
//  ICDownloadMembreController.swift
//  iClem
//
//  Created by Odric Roux-Paris on 05/03/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

import Cocoa
import Clem
class ICDownloadMembreController: NSViewController {

    var profileDownload: CProfileAll!
    
    @IBOutlet weak var progressBar: NSProgressIndicator!
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do view setup here.
        
        
        profileDownload.getAllProfile({ (o:Int, i:Int, error) -> Void in
            let progressValue:Int = 100 * i / o
            
            
            self.progressBar.doubleValue = Double(progressValue)
            
        }, allProfile: { (array, e) -> Void in
            self.dismissController(nil)
        })
    }
    
}
