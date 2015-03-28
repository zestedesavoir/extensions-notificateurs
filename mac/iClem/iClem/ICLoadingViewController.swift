//
//  ICLoadingViewController.swift
//  iClem
//
//  Created by Odric Roux-Paris on 28/03/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

import Cocoa

class ICLoadingViewController: NSViewController {

    @IBOutlet weak var loadingIndicator: NSProgressIndicator!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do view setup here.
    }
    override func viewDidAppear() {
        loadingIndicator.startAnimation(nil)
        self.view.window?.makeFirstResponder(self.view)
    
    }
    override func viewDidDisappear() {
        loadingIndicator.stopAnimation(nil)
    }
    
}
