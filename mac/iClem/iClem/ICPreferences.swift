//
//  ICPreferences.swift
//  iClem
//
//  Created by Odric Roux-Paris on 19/01/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//
// class qui implémente les préférences...

import Cocoa
/*
    Constantes pour les développeurs
*/
let ZDSConnectDefault = "ZDSConnectDefault"

/*
     D'initilisation des préférences
*/



func setBoolConnectDefault(connect:Bool){
    NSUserDefaults.standardUserDefaults().setBool(connect, forKey: ZDSConnectDefault)
}
func boolConnectDefault() -> Bool{
    return NSUserDefaults.standardUserDefaults().boolForKey(ZDSConnectDefault)
}



/*
    String pour chercher les préférences dans le dico
*/

/*
    Fonction qui fait office de setter et de getter aux préférences
*/

