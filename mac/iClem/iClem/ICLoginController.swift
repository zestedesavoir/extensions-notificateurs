//
//  ICLoginController.swift
//  iClem
//
//  Created by Odric Roux-Paris on 17/01/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

import Cocoa

class ICLoginController: NSViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do view setup here.
        self.view.window?.makeFirstResponder(self.view)
    }
    
}
