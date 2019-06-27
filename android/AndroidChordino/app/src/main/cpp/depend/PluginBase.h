//
// Created by Administrator on 2019/6/17.
//

#ifndef ANDROIDCHORDINO_PLUGINBASE_H
#define ANDROIDCHORDINO_PLUGINBASE_H


#include <string>
#include <vector>

namespace Vamp {

/**
 * A base class for plugins with optional configurable parameters,
 * programs, etc.  The Vamp::Plugin is derived from this, and
 * individual Vamp plugins should derive from that.
 *
 * This class does not provide the necessary interfaces to instantiate
 * or run a plugin.  It only specifies an interface for retrieving
 * those controls that the host may wish to show to the user for
 * editing.  It could meaningfully be subclassed by real-time plugins
 * or other sorts of plugin as well as Vamp plugins.
 */

    class PluginBase {
    public:
        virtual ~PluginBase() {}

        /**
         * Get the Vamp API compatibility level of the plugin.
         */
        virtual unsigned int getVampApiVersion() const { return 2; }

        /**
         * Get the computer-usable name of the plugin.  This should be
         * reasonably short and contain no whitespace or punctuation
         * characters.  It may only contain the characters [a-zA-Z0-9_-].
         * This is the authoritative way for a program to identify a
         * plugin within a given library.
         *
         * This text may be visible to the user, but it should not be the
         * main text used to identify a plugin to the user (that will be
         * the name, below).
         *
         * Example: "zero_crossings"
         */
        virtual std::string getIdentifier() const = 0;

        /**
         * Get a human-readable name or title of the plugin.  This
         * should be brief and self-contained, as it may be used to
         * identify the plugin to the user in isolation (i.e. without also
         * showing the plugin's "identifier").
         *
         * Example: "Zero Crossings"
         */
        virtual std::string getName() const = 0;

        /**
         * Get a human-readable description for the plugin, typically
         * a line of text that may optionally be displayed in addition
         * to the plugin's "name".  May be empty if the name has said
         * it all already.
         *
         * Example: "Detect and count zero crossing points"
         */
        virtual std::string getDescription() const = 0;

        /**
         * Get the name of the author or vendor of the plugin in
         * human-readable form.  This should be a short identifying text,
         * as it may be used to label plugins from the same source in a
         * menu or similar.
         */
        virtual std::string getMaker() const = 0;

        /**
         * Get the copyright statement or licensing summary for the
         * plugin.  This can be an informative text, without the same
         * presentation constraints as mentioned for getMaker above.
         */
        virtual std::string getCopyright() const = 0;

        /**
         * Get the version number of the plugin.
         */
        virtual int getPluginVersion() const = 0;


        struct ParameterDescriptor {
            /**
             * The name of the parameter, in computer-usable form.  Should
             * be reasonably short, and may only contain the characters
             * [a-zA-Z0-9_-].
             */
            std::string identifier;

            /**
             * The human-readable name of the parameter.
             */
            std::string name;

            /**
             * A human-readable short text describing the parameter.  May be
                 * empty if the name has said it all already.
             */
            std::string description;

            /**
             * The unit of the parameter, in human-readable form.
             */
            std::string unit;

            /**
             * The minimum value of the parameter.
             */
            float minValue;

            /**
             * The maximum value of the parameter.
             */
            float maxValue;

            /**
             * The default value of the parameter.  The plugin should
             * ensure that parameters have this value on initialisation
             * (i.e. the host is not required to explicitly set parameters
             * if it wants to use their default values).
             */
            float defaultValue;

            /**
             * True if the parameter values are quantized to a particular
             * resolution.
             */
            bool isQuantized;

            /**
             * Quantization resolution of the parameter values (e.g. 1.0
             * if they are all integers).  Undefined if isQuantized is
             * false.
             */
            float quantizeStep;

            /**
             * Names for the quantized values.  If isQuantized is true,
             * this may either be empty or contain one string for each of
             * the quantize steps from minValue up to maxValue inclusive.
             * Undefined if isQuantized is false.
             *
             * If these names are provided, they should be shown to the
             * user in preference to the values themselves.  The user may
             * never see the actual numeric values unless they are also
             * encoded in the names.
             */
            std::vector<std::string> valueNames;

            ParameterDescriptor() : // the defaults are invalid: you must set them
                    minValue(0),
                    maxValue(0),
                    defaultValue(0),
                    isQuantized(false),
                    quantizeStep(0) {}
        };

        typedef std::vector<ParameterDescriptor> ParameterList;

        /**
         * Get the controllable parameters of this plugin.
         */
        virtual ParameterList getParameterDescriptors() const {
            return ParameterList();
        }

        /**
         * Get the value of a named parameter.  The argument is the identifier
         * field from that parameter's descriptor.
         */
        virtual float getParameter(std::string) const { return 0.0; }

        /**
         * Set a named parameter.  The first argument is the identifier field
         * from that parameter's descriptor.
         */
        virtual void setParameter(std::string, float) {}


        typedef std::vector<std::string> ProgramList;

        /**
         * Get the program settings available in this plugin.  A program
         * is a named shorthand for a set of parameter values; changing
         * the program may cause the plugin to alter the values of its
         * published parameters (and/or non-public internal processing
         * parameters).  The host should re-read the plugin's parameter
         * values after setting a new program.
         *
         * The programs must have unique names.
         */
        virtual ProgramList getPrograms() const { return ProgramList(); }

        /**
         * Get the current program.
         */
        virtual std::string getCurrentProgram() const { return ""; }

        /**
         * Select a program.  (If the given program name is not one of the
         * available programs, do nothing.)
         */
        virtual void selectProgram(std::string) {}

        /**
         * Get the type of plugin.  This is to be implemented by the
         * immediate subclass, not by actual plugins.  Do not attempt to
         * implement this in plugin code.
         */
        virtual std::string getType() const = 0;
    };

}


#endif //ANDROIDCHORDINO_PLUGINBASE_H
